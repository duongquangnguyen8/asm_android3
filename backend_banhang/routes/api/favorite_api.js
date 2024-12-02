const express=require('express');
const router=express.Router();
const Favorite=require('../../models/favorite');
router.post('/add_favorite',async(req,res)=>{
    try{
        const data=req.body;
        const newFavorite= new Favorite({
            accountId: data.accountId,
            productId: data.productId,
            isFavorite: data.isFavorite,
        });
        const result= await newFavorite.save();
        res.status('200').json({
            status:'200',
            message: 'Thành công',
            data: result,
        });
    }catch(error){
        console.log(error);
    }
});
router.get('/get_all_favorite',async(req,res)=>{
    try{
        const data=await Favorite.find();
        res.status('200').json({
            status: '200',
            message: 'Get thành công',
            data: data
        })
    }catch(error){
        console.log(error);
    }
});
router.delete('/delete_favorite_byId/:id',async(req,res)=>{
    try{
        const {id}= req.params;
        const data= await Favorite.findByIdAndDelete(id);
        res.status('200').json({
            status: '200',
            message: 'thành công delete',
            data: data
        })
    }catch(error){
        console.log(error);
    }
});
router.delete('/deleteFavoriteByIdProduct/:idProduct',async(req,res)=>{
    try{
        const {idProduct}=req.params;
        const data=await Favorite.findOneAndDelete({productId: idProduct});
        res.status('200').json({
            status: '200',
            message: 'success',
            data: data,
        })
    }catch(error){
        console.log(error);
    }
})
//lấy thông tin của product thông qua id
module.exports=router;