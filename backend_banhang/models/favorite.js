const mongoose=require('mongoose');
const FavoriteSchema=new mongoose.Schema({
    accountId: {type: mongoose.Schema.ObjectId,ref: 'Account',required:true},
    productId: {type: mongoose.Schema.ObjectId,ref: 'Product',required: true},
    isFavorite: {type: Boolean,default: false}
},{
    timestamps: true,
});
const Favorite=mongoose.model('Favorite',FavoriteSchema);
module.exports=Favorite;