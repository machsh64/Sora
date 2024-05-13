package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api("员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result logout() {

        // 用户退出登录时,一处线程中的id
        BaseContext.removeCurrentId();
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("添加员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工：{}", employeeDTO);

        // 设置账号的状态,默认正常状态
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> getPage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询, 参数为：{}", employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用/禁用员工
     *
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用/禁用员工")
    public Result updateStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        log.info(status == 1 ? "启用" : "禁用" + "员工, 参数为：id: {} ", id);

        employeeService.updateStatus(status,id);
        return Result.success();
    }

    /**
     * 根据id获取员工数据
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id获取员工数据")
    public Result<Employee> getById(@PathVariable("id") Long id) {

        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }


    /**
     * 更新员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("更新员工信息, 参数为：{}", employeeDTO);

        employeeService.update(employeeDTO);
        return Result.success();
    }


}
