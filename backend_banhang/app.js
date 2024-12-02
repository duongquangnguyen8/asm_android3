var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var apiRole=require('./routes/api/role_api');
var apiAccount=require('./routes/api/account_api');
var apiCategory=require('./routes/api/category_api');
var apiProduct=require('./routes/api/product_api');
var apiCart=require('./routes/api/cart_api');
var apiCartDetail=require('./routes/api/cartDetail_api');
var apiFavorite=require('./routes/api/favorite_api');
var apiBill=require('./routes/api/bill_api');
var apiBillDetail=require('./routes/api/billDetail_api');

var database=require('./config/contextDb');
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/role',apiRole);
app.use('/account',apiAccount);
app.use('/category',apiCategory);
app.use('/product',apiProduct);
app.use('/cart',apiCart);
app.use('/cartDetail',apiCartDetail);
app.use('/favorite',apiFavorite);
app.use('/bill',apiBill);
app.use('/billDetail',apiBillDetail);

database.connect();
// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
