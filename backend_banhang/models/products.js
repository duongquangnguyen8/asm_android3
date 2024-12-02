const mongoose=require('mongoose');

const ProductSchema=new mongoose.Schema({
    productName: {type:String, required: true},
    description:{type: String},
    price: {type: Number},
    image: {type: String},
    categoryId: {type:mongoose.Schema.ObjectId,ref:'Category',required: true}
},
{
    timestamps:true,
});

const Product=mongoose.model('Product',ProductSchema);
module.exports=Product;