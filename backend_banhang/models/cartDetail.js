const mongoose=require('mongoose');

const CartDetailSchema=new mongoose.Schema({
    cartId: {type: mongoose.Schema.ObjectId,ref: 'Cart',required:true},
    productId:{type: mongoose.Schema.ObjectId,ref: 'Product',required:true},
    quantity: {type: Number},
    price: {type: Number},

},{
    timestamps: true,
});

const CartDetail=mongoose.model('CartDetail',CartDetailSchema);
module.exports=CartDetail;