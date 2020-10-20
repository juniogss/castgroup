require('dotenv').config();
const express = require('express');
const cors = require('cors');
const routes = require('./routes');
const bodyParser = require('body-parser');
const app = express();
let connectionString = '';

// DB connection
if (!process.env.DB_USER && !process.env.DB_PASSWORD){
    connectionString = process.env.DB_CLIENT + "://" + process.env.DB_HOST + ':' + process.env.DB_PORT + '/' + process.env.DB_DATABASE;
} else {
    connectionString = process.env.DB_CLIENT + "://" + process.env.DB_USER + ':' + process.env.DB_PASSWORD + '@' + process.env.DB_HOST + ':' + process.env.DB_PORT + '/' + process.env.DB_DATABASE;
}
var mongoose = require("mongoose");
mongoose.connect(connectionString, { useNewUrlParser: true, useUnifiedTopology: true }).then(() => {
    
    //don't show the log when it is test
	console.log("Connected to %s", connectionString);
    console.log("App is running ... \n");
    console.log("Press CTRL + C to stop the process. \n");
})
.catch(err => {
    console.error("App starting error:", err.message);
    process.exit(1);
});
var db = mongoose.connection;


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cors());

app.use(routes);

const port = process.env.PORT;
app.listen(port, function() {
    console.log(`API running on port ${port}!`);
});