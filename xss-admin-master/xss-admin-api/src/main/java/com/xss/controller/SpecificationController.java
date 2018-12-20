package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Member;
import com.xss.domain.Specification;
import com.xss.domain.SpecificationValue;
import com.xss.service.CityService;
import com.xss.service.ProductCategoryService;
import com.xss.service.SpecificationService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

/**
 *  商品规格管理接口
 * @author zzl
 * @since 2018-08-10
 */
@RestController
@RequestMapping("/api/specification")
@Api(description="商品规格管理模块")
public class SpecificationController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(SpecificationController.class);

    @Autowired
    private SpecificationService specificationService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CityService cityService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="商品规格列表", notes="分页展示规格列表，支持根据名称搜索",produces = "application/json")
    @Log(description="平台中心获取商品规格列表接口:/api/specification/list")
    public PageResult<Object> list(Pageable pageable,@CurrentUser Member member) {
        LOG.debug("get Specification list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
//            pageable.getFilters().add(Filter.eq("company", member.getCompany()));
            Page<Specification> data = specificationService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), specificationService.createEntity().convertList(data.getContent(), new String[]{"specificationValues"}));
        }catch (Exception e){
            LOG.error("get Specification list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Specification list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="商品规格详情", notes="根据id获取商品规格详情",produces = "application/json")
    @Log(description="平台中心获取商品规格详情接口:/api/specification/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get specification info param = " + id);

        Specification specification = specificationService.find(id);
        JSONObject result = specification.convertEntity(specification, new String[]{"specificationValues"});

        LOG.debug("get specification info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新商品规格", notes="保存商品规格",produces = "application/json")
    @Log(description="平台中心保存商品规格接口:/api/specification/save")
    public PublicResult<Boolean> save(@RequestBody JSONObject specJo, @CurrentUser Member member)throws Exception{
        Specification specification = JsonUtil.json2Obj(specJo.toJSONString(), Specification.class);
        for (Iterator<SpecificationValue> iterator = specification.getSpecificationValues().iterator(); iterator.hasNext();) {
            SpecificationValue specificationValue = iterator.next();
            if (specificationValue == null || specificationValue.getName() == null) {
                iterator.remove();
            } else {
                if (specification.getType() == Specification.Type.text) {
                    specificationValue.setImage(null);
                }
                specificationValue.setSpecification(specification);
            }
        }
        specification.setProductCategory(productCategoryService.find(specification.getProductCategory().getId()));
//        if(StringUtils.isBlank(specification.getCityCode())) {
//            specification.setCityCode(cityService.getCityCodeByAdmin(admin));
//        }
        if (null != specification && null != specification.getId()) {
            specificationService.update(specification,"products", "company");
        }else{
            specification.setProducts(null);
//            specification.setCompany(member.getCompany());
            specificationService.save(specification);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除商品规格", notes="批量删除商品规格",produces = "application/json")
    @Log(description="平台中心批量删除商品规格接口:/api/specification/delete")
    public PublicResult<String> delete(Long[] ids) {
        LOG.info("delete specification param: " + ids);
        try {
            specificationService.delete(ids);
            return new PublicResult<>(PublicResultConstant.SUCCESS, "删除成功");
        } catch (Exception e) {
            LOG.error("delete specification error. {}", e);
            return new PublicResult<String>(PublicResultConstant.FAILED, "删除失败，规格已在商品中使用");
        }
    }
}
