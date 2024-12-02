const express = require("express");
const router = express.Router();
const Account = require("../../models/account");
const Role = require("../../models/role");
router.post("/add_account", async (req, res) => {
  try {
    const data = req.body;
    console.log("Received roleId:", data.roleId);
    const roleExists = await Role.findById(data.roleId);
    console.log("Role Exists:", roleExists);
    if (!roleExists) {
      return res.status(400).json({
        status: "400",
        message: "Role không tồn tại",
      });
    }
    const newAccount = new Account({
      email: data.email,
      pass: data.pass,
      fullName: data.fullName,
      address: data.address,
      phoneNumber: data.phoneNumber,
      birth: data.birth,
      roleId: data.roleId,
    });
    const result = await newAccount.save();
    res.status("200").json({
      status: "200",
      message: "Thành công",
      data: result,
    });
  } catch (error) {
    console.log(error);
  }
});
router.get('/get_all_account',async(req,res)=>{
  try{
    const data=await Account.find();
    res.status('200').json({
      status:'200',
      message: 'Thành công get',
      data: data,
    })
  }catch(error){
    console.log(error);
  }
});
router.get('/getAccountById/:id',async(req,res)=>{
  try{
    const {id}=req.params;
    const data=await Account.findById(id);
    res.status('200').json({
      status: '200',
      message: 'Thành công getid',
      data: data,
    });
  }catch(error){
    console.log(error);
  }
});
router.put('/updateAccountById/:id',async (req,res)=>{
  try{
    const {id}=req.params;
    const updateData=req.body;
    const account=await Account.findById(id);
    Object.assign(account,updateData); //lấy thông tin body vào để update
    const result=await account.save();
    res.status("200").json({ 
      status: "200",
       message: "Cập nhật thành công", 
       data: result,
    });
  }catch(error){
    console.log(error);
  }
});
router.get('/getAccountByIdRole/:idRole',async(req,res)=>{
  const {idRole}=req.params;
  try{
    const data=await Account.find({roleId: idRole});
    res.status('200').json({
      status: '200',
      message: 'success',
      data: data,
    })
  }catch(error){
    console.log(error);
  }
})
module.exports = router;
