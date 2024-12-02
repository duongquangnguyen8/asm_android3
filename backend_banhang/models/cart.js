const mongoose=require('mongoose');
const Account=require('./account');
const CartSchema=new mongoose.Schema({
    accountId: {type: mongoose.Schema.ObjectId,ref: 'Account',required:true},
    shippingAddress: {type:String},
    shippingPhone: {type: String},
},{
    timestamps:true,
});
CartSchema.pre('save',async function (next) {
    if(this.isNew){
        try{
            const account=await Account.findById(this.accountId);
            if(account){
                this.shippingAddress=account.address;
                this.shippingPhone=account.phoneNumber;
            }
        }catch(error){
            return next(error);
        }
        next();
    };
})
const Cart=mongoose.model('Cart',CartSchema);
module.exports=Cart;