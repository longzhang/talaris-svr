package me.ele.talaris.webapi.allowance;

import java.util.List;

import me.ele.talaris.model.Allowance;
import me.ele.talaris.model.Context;
import me.ele.talaris.service.allowance.IGetAllowanceInfoService;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/webapi")
public class AllowanceController {
    @Autowired
    IGetAllowanceInfoService getAllowanceInfoService;

    @RequestMapping("allowance/")
    @InterfaceMonitor(interfaceName = "获取补贴信息接口", contextIndex = 0)
    public @ResponseBody ResponseEntity<List<Allowance>> getAllowanceInfo(Context context) {
        List<Allowance> allowances = getAllowanceInfoService.getAllowanceInfo(context);
        return ResponseEntity.success(allowances);
    }

}
