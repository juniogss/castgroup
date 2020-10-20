var mongoose = require("mongoose");

var CourseSchema = new mongoose.Schema({
	subjectDesc: {type: String, required: true},
	startDate: {type: Date, required: true},
	endDate: {type: Date, required: true},
    students: {type: Number},
    fkCategory: {type: Number, required: true},
}, {timestamps: true});

module.exports = mongoose.model("course", CourseSchema, "course");