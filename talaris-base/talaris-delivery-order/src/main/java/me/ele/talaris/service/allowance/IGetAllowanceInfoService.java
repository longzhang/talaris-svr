package me.ele.talaris.service.allowance;

import java.util.List;

import me.ele.talaris.model.Allowance;
import me.ele.talaris.model.Context;

public interface IGetAllowanceInfoService {
    
    List<Allowance> getAllowanceInfo(Context context);

}
