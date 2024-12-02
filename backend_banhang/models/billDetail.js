const mongoose=require('mongoose');
const BillDetailSchema=new mongoose.Schema({
    billId: {type: mongoose.Schema.ObjectId,ref:'Bill',required:true},
    productId: {type: mongoose.Schema.ObjectId,ref:'Product',required:true},
    quantity: {type: Number},
    price: {type: Number},
},{
    timestamps:true,
});
const BillDetail=mongoose.model('BillDetail',BillDetailSchema);
module.exports=BillDetail;