const express = require('express');
const routes = express.Router();
const CategoryController = require('./controllers/category');
const CourseController = require('./controllers/course');

// category endpoints
routes.get('/category', CategoryController.list);
routes.post('/category', CategoryController.create);
routes.put('/category/:code', CategoryController.edit);
routes.delete('/category/:code', CategoryController.remove);

// course endpoints
routes.get('/course', CourseController.list);
routes.post('/course', CourseController.create);
routes.put('/course/:id', CourseController.edit);
routes.delete('/course/:id', CourseController.remove);

module.exports = routes;