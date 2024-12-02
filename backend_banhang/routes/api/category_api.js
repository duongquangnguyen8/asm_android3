const express=require('express');
const router=express.Router();
const Category=require('../../models/category');
const { route } = require('..');

router.post('/add_category',async(req,res)=>{
   try{
    const data=req.body;
    const newCategory=new Category({
        nameCategory: data.nameCategory,
    });
    const result=await newCategory.save();
    res.status('200').json({
        status: '200',
        message: 'Add category thành công',
        data: result,
    })
   }catch(error){
    console.log(error);
   }

});
module.exports=router;