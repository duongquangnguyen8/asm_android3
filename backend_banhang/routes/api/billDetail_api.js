const express=require('express');
const router=express.Router();
const BillDetail=require('../../models/billDetail');

router.post('/add_billDetail',async(req,res)=>{
    try{
        const data=req.body;
        const newBillDetail=new BillDetail({
            billId: data.billId,
            productId: data.productId,
            quantity: data.quantity,
            price: data.price,
        });
        const result=await newBillDetail.save();
        res.status('200').json({
            status: '200',
            message: 'Thành công',
            data: result,
        });
    }catch(error){
        console.log(error);
    }
});
router.delete('/deteBillDetailById/:id',async(req,res)=>{
    try{
        const {id}=req.params;
        const data=await BillDetail.findByIdAndDelete(id);
        res.status('200').json({
            status: '200',
            message: 'delete success',
            data: data,
        })
    }catch(error){
        console.log(error);
    }
});
router.get('/getBillDetailByIdBill/:idBill',async(req,res)=>{
    try{
        const {idBill}=req.params;
        const data=await BillDetail.find({billId:idBill});
        res.status('200').json({
            status:'200',
            message: 'success',
            data: data,
        })
    }catch(error){
        console.log(error);
    }
})
module.exports=router;