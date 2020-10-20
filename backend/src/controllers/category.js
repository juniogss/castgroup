const CategoryModel = require("../models/category");
const { body, validationResult } = require("express-validator");

//helper file to prepare responses.
const apiResponse = require("../helpers/apiResponse");


/**
 * Category list.
 *
 * @param {number}      code
 * @param {string}      desc
 *
 * @returns {Array}
 */
exports.list = [
	// Process request after validation and sanitization.
	(req, res) => {
		try {

			const fields = { _id: 1, code: 1, desc: 1 };

			CategoryModel.find({}, fields).then((categories) => {
				if (categories.length > 0) {
					return apiResponse.successResponseWithData(res, "Lista de categorias", categories);
				} else {
					return apiResponse.successResponseWithData(res, "Lista de categorias", []);
				}
			});

		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];


/**
 * Category created.
 *
 * @param {number}      code
 * @param {string}      desc
 *
 * @returns {Object}
 */

exports.create = [
	// Validate fields.
    body("code").isLength({ min: 1 }).trim().withMessage("Código da categoria deve ser informado.")
        .custom((value) => {
            return CategoryModel.findOne({code : value}).then((category) => {
                if (category) {
                    return Promise.reject("Código da categoria já utilizado.");
                }
            });
        }),
    body("desc").isLength({ min: 1 }).withMessage("Descrição da categoria deve ser informado."),
	
    // Sanitize fields.
    body("code").escape(),
	body("desc").escape(),

	// Process request after validation and sanitization.
	(req, res) => {
		try {
			// Extract the validation errors from a request.
			const errors = validationResult(req);

			if (!errors.isEmpty()) {
				// Display sanitized values/errors messages.
				return apiResponse.validationErrorWithData(res, "Validation Error.", errors.array());
			} else {

				// Create category object with escaped and trimmed data
				let category = new CategoryModel(
					{
                        code: req.body.code,
						desc: req.body.desc
						
					}
				);

				category.save(function (err) {
					if (err) { return apiResponse.ErrorResponse(res, err); }
					let categoryData = {
						code: category.code,
						desc: category.desc
					};
					return apiResponse.successResponseWithData(res, "Categoria adicionada com sucesso.", categoryData);
				});
			}
		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];


/**
 * Category edit.
 *
 * @param {number}      code
 * @param {string}      desc
 *
 * @returns {Array}
 */
exports.edit = [

    // Validate fields.
    body("code").isLength({ min: 1 }).trim().withMessage("Código da categoria deve ser informado."),
    body("desc").isLength({ min: 1 }).withMessage("Descrição da categoria deve ser informado."),
	
    // Sanitize fields.
    body("code").escape(),
    body("desc").escape(),
    
	(req, res) => {
		try {
			// Extract the validation errors from a request.
			const errors = validationResult(req);

			if (!errors.isEmpty()) {
				// Display sanitized values/errors messages.
				return apiResponse.validationErrorWithData(res, "Validation Error.", errors.array());
			} else {

                const query = { code: req.params.code };
                CategoryModel.findOneAndUpdate(query, { $set: { desc: req.body.desc }}, {new: true}, (err, doc) => {
                    if (err) {
                        return apiResponse.ErrorResponse(res, err);
                    }
                
                    return apiResponse.successResponse(res, "Categoria alterada com sucesso.");
                });
			}
		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];


/**
 * Category remove.
 *
 * @param {number}      code
 *
 * @returns {Array}
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

                const query = { code: req.params.code };
                CategoryModel.findOneAndRemove(query, (err, doc) => {
                    if (err) {
                        return apiResponse.ErrorResponse(res, err);
                    }
                
                    return apiResponse.successResponse(res, "Categoria deletada com sucesso.");
                });
			}
		} catch (err) {
			//throw error in json response with status 500.
			return apiResponse.ErrorResponse(res, err);
		}
	}
];