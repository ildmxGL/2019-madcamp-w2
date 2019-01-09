var mongoose 	= require('mongoose');
var Schema 		= mongoose.Schema;

var contactsSchema = new Schema( {
	name: String,
	phone_number: {
		type: String,
		unique: true,
	}
} );

module.exports = mongoose.model('contacts', contactsSchema);
