var mongoose = require("mongoose");

var CategorySchema = new mongoose.Schema({
    code: {type: Number, required: true},
    desc: {type: String, required: true},
}, {timestamps: true});

module.exports = mongoose.model("category", CategorySchema, "category");