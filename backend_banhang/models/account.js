const mongoose=require('mongoose');

const AccountSchema=new mongoose.Schema({
    email: {type: String, unique: true,required: true},
    pass: {type: String, required: true},
    fullName: {type: String},
    address: {type:String},
    phoneNumber:{type:String},
    birth: {type: Date},
    roleId:{type:mongoose.Schema.Types.ObjectId, ref:'Role',required:true},
},{
    timestamps: true,
});
const Account=mongoose.model('Account',AccountSchema);
module.exports=Account;