package me.ele.talaris.service.allowance.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ele.talaris.dao.AllowanceDao;
import me.ele.talaris.model.Allowance;
import me.ele.talaris.model.Context;
import me.ele.talaris.service.allowance.IGetAllowanceInfoService;

@Service
public class GetAllowanceInfoService implements IGetAllowanceInfoService {
    @Autowired
    AllowanceDao allowanceDao;

    @Override
    public List<Allowance> getAllowanceInfo(Context context) {
        return allowanceDao.getAll();
    }

}
