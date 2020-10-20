const CourseModel = require("../models/course");
const CategoryModel = require("../models/category");
const { body, validationResult } = require("express-validator");
const { ObjectID, ObjectId } = require("mongodb");

//helper file to prepare responses.
const apiResponse = require("../helpers/apiResponse");

/**
 * Courses list.
 *
 * @param {string}      subjectDesc
 * @param {Date}        startDate
 * @param {Date}        endDate
 * @param {number}      students
 * @param {number}      fkCategory
 *
 * @returns {Array}
 */
exports.list = [
	// Process request after validation and sanitization.
	(req, res) => {
		try {

			const fields = { _id: 1, subjectDesc: 1, startDate: 1, endDate: 1, students: 1, fkCategory: 1};

			CourseModel.find({}, fields).then((courses) => {
				if (courses.length > 0) {
					return apiResponse.successResponseWithData(res, "Lista de cursos", courses);
				} else {
					return apiResponse.successResponseWithData(res, "Lista de cursos", []);
				}
			});

		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];


/**
 * Courses created.
 *
 * @param {string}      subjectDesc
 * @param {Date}        startDate
 * @param {Date}        endDate
 * @param {number}      students
 * @param {number}      fkCategory
 *
 * @returns {Object}
 */

exports.create = [
	// Validate fields.
    body("subjectDesc").isLength({ min: 1 }).withMessage("Descrição do curso deve ser informada."),
    body("startDate").custom((startDate, { req }) => {

        // Fetch year, month and day of respective dates  
        const [sd, sm, sy] = startDate.split('/');
        const [ed, em, ey] = req.body.endDate.split('/');

        // Constructing dates from given string date input 
        const sDate = new Date(sy, sm, sd);
        const eDate = new Date(ey, em, ed);
        const nowDate = new Date();

        if (sDate < nowDate) {
            throw new Error('Data de início deve ser maior que a data atual.');
        }

        if (sDate > eDate) {
            throw new Error('Data de início do curso deve ser menor ou igual que a data final do curso.');
        }
        return true;
    }),
    body("endDate").custom((endDate, { req }) => {

        // Fetch year, month and day of respective dates  
        const [sd, sm, sy] = req.body.startDate.split('/');
        const [ed, em, ey] = endDate.split('/');

        // Constructing dates from given string date input 
        const sDate = new Date(sy, sm, sd);
        const eDate = new Date(ey, em, ed);

        if (eDate < sDate) {
            throw new Error('Data final do curso deve ser maior que a data de início do curso.');
        }
        return true;
    }),
    body("startDate").custom((startDate, { req }) => {

        // Fetch year, month and day of respective dates  
        const [sd, sm, sy] = startDate.split('/');
        const [ed, em, ey] = req.body.endDate.split('/');

        // Constructing dates from given string date input 
        const sDate = new Date(sy, sm-1, sd);
        const eDate = new Date(ey, em-1, ed);

        return CourseModel.find({ 
            $or: [
                {
                    startDate: {$gte: sDate, $lte: eDate}
                },
                {
                    endDate: {$gte: sDate, $lte: eDate}
                }
            ]})
            .then((course) => {
                if (course.length > 0) {
                    return Promise.reject("Existe(m) curso(s) planejado(s) dentro do período informado.");
                }
            });
    }),
    body("fkCategory").isLength({ min: 1 })
        .custom((value) => {
            return CategoryModel.findOne({code : value}).then((category) => {
                if (!category) {
                    return Promise.reject("Código da categoria não encontrado.");
                }
            });
        }),
	
    // Sanitize fields.
    body("subjectDesc").escape(),
    body("students").escape(),
    body("fkCategory").escape(),

	// Process request after validation and sanitization.
	(req, res) => {
		try {
            
			// Extract the validation errors from a request.
			const errors = validationResult(req);

			if (!errors.isEmpty()) {
				// Display sanitized values/errors messages.
				return apiResponse.validationErrorWithData(res, "Validation Error.", errors.array());
			} else {

                // Fetch year, month, day of respective dates 
                const [sd, sm, sy] = req.body.startDate.split('/');
                const [ed, em, ey] = req.body.endDate.split('/');

				// Create course object with escaped and trimmed data
				let course = new CourseModel(
					{
                        subjectDesc: req.body.subjectDesc,
                        startDate: new Date(sy, sm-1, sd).toDateString(),
                        endDate: new Date(ey, em-1, ed).toDateString(),
                        students: req.body.students,
                        fkCategory: req.body.fkCategory
					}
				);

				course.save(function (err) {
					if (err) { return apiResponse.ErrorResponse(res, err); }
					let courseData = {
						subjectDesc: course.subjectDesc,
                        startDate: course.startDate,
                        endDate: course.endDate,
                        students: course.students,
                        fkCategory: course.fkCategory
					};
					return apiResponse.successResponseWithData(res, "Curso adicionado com sucesso.", courseData);
				});
			}
		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];


/**
 * Courses edit.
 *
 * @param {string}      subjectDesc
 * @param {Date}        startDate
 * @param {Date}        endDate
 * @param {number}      students
 * @param {number}      fkCategory
 *
 * @returns {Object}
 */
exports.edit = [

    // Validate fields.
    body("subjectDesc").isLength({ min: 1 }).withMessage("Descrição do curso deve ser informada."),
    body("startDate").custom((startDate, { req }) => {

        // Fetch year, month and day of respective dates  
        const [sd, sm, sy] = startDate.split('/');
        const [ed, em, ey] = req.body.endDate.split('/');

        // Constructing dates from given string date input 
        const sDate = new Date(sy, sm, sd);
        const eDate = new Date(ey, em, ed);
        const nowDate = new Date();

        if (sDate < nowDate) {
            throw new Error('Data de início deve ser maior que a data atual.');
        }

        if (sDate >= eDate) {
            throw new Error('Data de início do curso deve ser menor ou igual que a data final do curso.');
        }
        return true;
    }),
    body("endDate").custom((endDate, { req }) => {

        // Fetch year, month and day of respective dates  
        const [sd, sm, sy] = req.body.startDate.split('/');
        const [ed, em, ey] = endDate.split('/');

        // Constructing dates from given string date input 
        const sDate = new Date(sy, sm, sd);
        const eDate = new Date(ey, em, ed);

        if (eDate < sDate) {
            throw new Error('Data final do curso deve ser maior que a data de início do curso.');
        }
        return true;
    }),
    
    body("fkCategory").isLength({ min: 1 })
        .custom((value) => {
            return CategoryModel.findOne({code : value}).then((category) => {
                if (!category) {
                    return Promise.reject("Código da categoria não encontrado.");
                }
            });
        }),
	
    // Sanitize fields.
    body("subjectDesc").escape(),
    body("students").escape(),
    body("fkCategory").escape(),
    
	(req, res) => {
		try {
			// Extract the validation errors from a request.
			const errors = validationResult(req);

			if (!errors.isEmpty()) {
				// Display sanitized values/errors messages.
				return apiResponse.validationErrorWithData(res, "Validation Error.", errors.array());
			} else {

                // Fetch year, month, day of respective dates 
                const [sd, sm, sy] = req.body.startDate.split('/');
                const [ed, em, ey] = req.body.endDate.split('/');

                CourseModel.findByIdAndUpdate(ObjectID(req.params.id), { 
                    $set: { 
                        subjectDesc: req.body.subjectDesc,
                        startDate: new Date(sy, sm-1, sd).toDateString(),
                        endDate: new Date(ey, em-1, ed).toDateString(),
                        students: req.body.students,
                        fkCategory: req.body.fkCategory
                    }}, {new: true}, (err, doc) => {
                    if (err) {
                        return apiResponse.ErrorResponse(res, err);
                    }
                
                    return apiResponse.successResponse(res, "Curso alterado com sucesso.");
                });
			}
		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];


/**
 * Courses remove.
 *
 * @param {ObjectId}      _id
 *
 * @returns {Object}
 */
exports.remove = [
	(req, res) => {
		try {
			// Extract the validation errors from a request.
			const errors = validationResult(req);

			if (!errors.isEmpty()) {
				// Display sanitized values/errors messages.
				return apiResponse.validationErrorWithData(res, "Validation Error.", errors.array());
			} else {

                CourseModel.findByIdAndRemove(ObjectID(req.params.id), (err, doc) => {
                    if (err) {
                        return apiResponse.ErrorResponse(res, err);
                    }
                
                    return apiResponse.successResponse(res, "Curso deletado com sucesso.");
                });
			}
		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];