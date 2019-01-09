var express 	= require('express');
var router 	 	= express.Router();
var upload 		= require('./upload');
var mongoose 	= require('mongoose');
var path 		= require('path');
var Photo 		= require("../models/Photo");
var fs 			= require('fs');

router.use(express.static(path.join(__dirname, 'public')));
/**
 * Get gallery web page
 */
router.get('/', function(req, res, next) {
	Photo.find({}, ['path', 'caption'], {sort: { _id: -1} }, function(err, photos) {
		res.render('gallery', { title: 'NodeJS file upload tutorial', msg: req.query.msg, photolist: photos });
	} );
} );

/**
 * Upload file to path and add record to dataas
 */
router.post('/upload', function(req, res) {
	upload(req, res, (error) => {
		if(error) {
			res.redirect('/gallery/?msg=3');
		} else {
			if(req.file == undefined) {
				res.redirect('/gallery/?msg=2');
			} else {
				var fullPath = "files/" + req.file.filename;
				var fullUrl = "http://socrip2.kaist.ac.kr:5080/gallery/" + fullPath;

				var document = {
					path: 		fullPath,
					caption:	req.body.caption,
					url: 		fullUrl
				};

				var photo = new Photo(document);
				photo.save(function(error) {
					if(error) {
						throw error;
					}
					res.redirect('/gallery/?msg=1');
				} );
			}
		}
	} );
} );

/**
 * Get All image list
 */
router.get('/list', function(req, res) {
	Photo.find( {}, { "_id": 1, "path": 1, "url": 1 }, function(err, photo) {
		if(err) return res.status(500).json( { error: err } );
		if(!photo) return res.tatus(404).json( { error: 'photo not found' });
		res.json(photo);
	} );
} );

/**
 * For deveoloper,
 * Delete all images
 */
router.delete('/delete', function(req, res) {
	Photo.remove( {}, function(err, output) {
		if(err) return res.status(500).json( { error: 'Database failure' } );
		console.log("Deleted all images");
		res.status(204).end();
	} );
} );


module.exports = router;
