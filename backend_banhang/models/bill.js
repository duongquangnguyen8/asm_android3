const mongoose=require('mongoose');
const Cart =require('./cart');
const BillSchema=new mongoose.Schema({
    accountId: {type: mongoose.Schema.ObjectId,ref: ('Account'),required: true},
    cartId: {type: mongoose.Schema.ObjectId,ref: ('Cart'),required: true},
    totalPrice: {type: Number},
    statusBill: {type:String},
    shippingName:{type:String},
    shippingAddress: {type: String},
    shippingPhone: {type: String},
},{
    timestamps:true,
});

const Bill=mongoose.model('Bill',BillSchema);
module.exports=Bill;


