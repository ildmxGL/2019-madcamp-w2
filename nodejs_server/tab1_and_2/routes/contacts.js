var express 	= require('express');
var router 		= express.Router();
var Contacts 	= require("../models/contacts");

// Get all address
router.get('/', function(req, res) {
	Contacts.find( {}, { "__v": 0 }, function(err, contacts) {
		if(err) return res.status(500).send({ error: 'Database failure' });
		res.json(contacts);
	});
});

// Create an address
router.post('/', function(req, res) {
	var contacts = new Contacts();
	contacts.name = req.body.name;
	contacts.phone_number = req.body.phone_number;

	contacts.save(function(err) {
		if(err) {
			console.error(err);
			res.json( { result: 0 });
			return;
		}

		res.json( { result: 1 });
	});
});

// Update the address
router.put('/:_id', function(req, res) {
	Contacts.update( { _id: req.params._id }, { $set: req.body }, function(err, output) {
		if(err) res.status(500).json( { error: 'Database failure' });
		console.log(output);
		if(!output.n) return res.status(404).json( { error: 'Contacts not found' });
		res.json( { message: 'Address updated' });
	});
});

// Delete the address
router.delete('/:_id', function(req, res) {
	Contacts.remove( { _id: req.params._id }, function(err, output) {
		if(err) return res.status(500).json( { error: 'Database failure' });
		res.status(204).end();
	});
});


module.exports = router;
