var express 	= require('express');
var router 		= express.Router();

router.get('/', function(req, res, next) {
	res.send('w2 server');
});

module.exports = router;
