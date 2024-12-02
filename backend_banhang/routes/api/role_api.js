const express=require('express');
const router=express.Router();
const Role=require("../../models/role");
const { route } = require('..');

router.post('/add_role',async(req,res)=>{
    try{
        const data=req.body;
        const newRole=new Role({
            name: data.name,
        });
        const result=await newRole.save();
        res.status(200).json({
            status:200,
            message:'Thêm role thành công',
            data: result,
        })
    }catch(error){
        console.log(error);
    }
});
module.exports=router;