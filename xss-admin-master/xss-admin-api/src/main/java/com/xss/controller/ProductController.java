package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.annotation.ParamXssPass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.*;
import com.xss.domain.enums.ProductType;
import com.xss.service.*;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品管理接口
 *
 * @author fan.hu
 * @since 2018-08-24
 */
@RestController
@RequestMapping("/api/product")
@Api(description = "商品管理模块")
public class ProductController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ProductController.class);

    @Autowired
    private ProductService productService;
    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private ParameterGroupService parameterGroupService;
    @Resource
    private AttributeService attributeService;
    @Resource
    private BrandService brandService;
    @Resource
    private TagService tagService;
    @Resource
    private SpecificationService specificationService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private SpecificationValueService specificationValueService;
    @Resource
    private CityService cityService;


    @ApiOperation(value = "异步根据商品分类获取指定的商品参数Json列表数据", notes = "根据商品分类获取指定的商品参数Json列表数据", produces = "application/json")
    @GetMapping("/parameter_groups")
    @Log(description = "异步根据商品分类获取指定的商品参数Json列表数据:/business/product/parameter_groups")
    public PublicResult<JSONArray> getParameterGroupsByProductCategory(ProductCategory productCategory, @CurrentUser Member member) throws Exception {
        JSONArray parameterGroupJa = new JSONArray();
        if (null != productCategory.getId()) {
            productCategory = productCategoryService.find(productCategory.getId());
            if (productCategory.getParameterGroups().isEmpty()) {
                productCategory = productCategory.getParent();
            }
            parameterGroupJa = parameterGroupService.getJsonListByProductCategoryAndCampany(productCategory);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, parameterGroupJa);
    }

    @ApiOperation(value = "根据商品分类获取指定的商品属性Json列表数据", notes = "根据商品分类获取指定的商品属性Json列表数据", produces = "application/json")
    @GetMapping("/attributes")
    @Log(description = "根据商品分类获取指定的商品属性Json列表数据:/business/product/parameter_groups")
    public PublicResult<JSONArray> getAttributesByProductCategory(ProductCategory productCategory, @CurrentUser Member member) throws Exception {
        JSONArray attributeJa = new JSONArray();
        if (null != productCategory.getId()) {
            productCategory = productCategoryService.find(productCategory.getId());
            if (productCategory.getAttributes().isEmpty()) {
                productCategory = productCategory.getParent();
            }
            attributeJa = attributeService.findByProductCategoryAndCompany(productCategory);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, attributeJa);
    }

    @ApiOperation(value = "获取商品规格初始化Json数据", notes = "获取商品规格初始化Json数据", produces = "application/json")
    @GetMapping("/specification")
    @Log(description = "获取商品规格初始化Json数据:/business/product/specification")
    public PublicResult<JSONObject> getSpecification(ProductCategory productCategory, @CurrentUser Member member) throws Exception {
        JSONObject specificationJo = new JSONObject();
        if (null != productCategory.getId()) {
            productCategory = productCategoryService.find(productCategory.getId());
            if (productCategory.getSpecifications().isEmpty()) {
                productCategory = productCategory.getParent();
            }
        }
        specificationJo = specificationService.getJsonObject(productCategory);
        return new PublicResult<>(PublicResultConstant.SUCCESS, specificationJo);
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value = "根据商品ID获取商品详情", notes = "根据商品ID获取商品详情", produces = "application/json")
    @Log(description = "企业中心根据商品ID获取商品详情:/business/product/info")
    public PublicResult<JSONObject> info(@RequestParam Long id) {
        LOG.info("product info param : id = " + id);
        PublicResult<JSONObject> result = null;
        try {
            Product product = productService.find(id);
            if (product == null) {
                return new PublicResult<JSONObject>(PublicResultConstant.PARAM_ERROR, null);
            }
            JSONArray parameterGroupJa = parameterGroupService.getJsonListByProductCategory(product.getProductCategory(), product.getParameterValue());
            JSONArray attributeJa = attributeService.getJsonListByProduct(product);
            JSONObject specificationJo = specificationService.getJsonObject(product);

            JSONObject jo = product.convertEntity(product, new String[]{"brand", "isMarketable", "isTop", "isGift", "isList", "isList", "deliveryTime", "deliveryPlace", "isLimit", "productImages", "productFreights", "relationProducts", "introduction", "tagIds"});
            jo.put("parameterGroups", parameterGroupJa);
            jo.put("attributeOptions", attributeJa);
            jo.put("specification", specificationJo);
            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, jo);
        } catch (Exception e) {
            LOG.error("product info error." + e);
            result = new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
        }
        return result;
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "商品列表", notes = "分页展示列表，支持根据名称搜索", produces = "application/json")
    @Log(description = "企业中心获取商品列表接口:/business/product/list")
    public PageResult<Object> list(@CurrentUser Member member, Pageable pageable) {
        LOG.debug("get Product list param = " + pageable.toString());
        PageResult<Object> result = null;
        try {
//            Store store = storeService.find(member.getStore().getId());
//            pageable.getFilters().add(new Filter("store", Filter.Operator.eq, store));
            Page<Product> data = productService.findPage(pageable);
            result = new PageResult<Object>((int) data.getTotal(), data.getPageNumber(), data.getPageSize(), productService.createEntity().convertList(data.getContent(), new String[]{"isMarketable", "isLimit", "deliveryTime", "deliveryPlace"}));
        } catch (Exception e) {
            LOG.error("get Product list error. {}", e);
            result = new PageResult<Object>(0, pageable.getPageNumber(), pageable.getPageSize(), null);
        }
        LOG.debug("get Product list result = " + result.toString());
        return result;
    }

    @ApiOperation(value = "异步获取发布商品相关初始化信息", notes = "异步获取发布商品相关初始化信息", produces = "application/json")
    @GetMapping("/getAddProductInitInfo")
    @Log(description = "异步获取发布商品相关初始化信息接口:/business/product/getAddProductInitInfo")
    public PublicResult<JSONObject> getAddProductInitInfo(@CurrentUser Member member) {
        JSONObject initJo = new JSONObject();
        try {
            //商品分类
//        JSONArray pcJa = new JSONArray();
//        List<ProductCategory> productCategoryTree = productCategoryService.findTree();
//
//        initJo.put("productCategoryTree", productCategoryService.convertProductCategories(productCategoryTree));

            //
//        List<Brand> brands = brandService.findAll();
//        initJo.put("brands", brandService.createEntity().convertList(brands, Brand.DEFAULT_JSON_PARAMS));

            //标签
            List<Tag> tags = tagService.findList(Tag.Type.product);
            JSONArray tagJa = new JSONArray();
            if (null != tags) {
                tagJa = tagService.createEntity().convertList(tags, null);
            }
            initJo.put("tags", tagJa);
            //店铺
//            initJo.put("store", member.getStore().getName());

//            //商品类型，只有自营企业才能发布监理检测商品
//            StoreCategory storeCategory = member.getStore().getStoreCategory();

            JSONArray productTypeJa = new JSONArray();
            for(ProductType productType : ProductType.values()){
//                if (StoreCategory.zy.equals(storeCategory) || !(productType.equals(ProductType.detection) || productType.equals(ProductType.supervisor))){
                    JSONObject productTypeJo = new JSONObject();
                    productTypeJo.put("name", productType.name());
                    productTypeJo.put("desc", productType.getDesc());
                    productTypeJa.add(productTypeJo);
//                }
            }
            initJo.put("productTypes", productTypeJa);

            // 审核通过的社区列表
            JSONArray communityJa = new JSONArray();
            List<Filter> filters =new ArrayList<>();
            // 审核通过
//            filters.add(Filter.eq("status", CommunityStatus.approved));
//            List<Community> communityList = communityService.findList(null, null, filters, null);
//            for(Community community : communityList){
//                JSONObject communityJo = new JSONObject();
//                communityJo.put("name", community.getName());
//                communityJo.put("id", community.getId());
//                communityJa.add(communityJo);
//            }
            initJo.put("communityList", communityJa);

            //会员等级
//        List<MemberRank> memberRanks = memberRankService.findAll();
//        JSONArray memberRankJa = new JSONArray();
//        if (null != memberRanks && !memberRanks.isEmpty()){
//            for(MemberRank memberRank : memberRanks){
//                JSONObject memberRankJo = JsonUtil.toJSONObject(memberRank, new String[]{"id", "name"});
//                memberRankJa.add(memberRankJo);
//            }
//        }
//        initJo.put("memberRanks", memberRankJa);

            //店铺列表
//        List<Filter> filters = new ArrayList<Filter>();
//        //filters.add(Filter.eq("storeStatus", StoreStatus.pass));
//        List<Order> orders = new ArrayList<Order>();
//        orders.add(Order.asc("storeCategory"));
//        List<Store> stores = storeService.findList(null,100, filters, orders);
//        JSONArray storeJa = new JSONArray();
//        if (null != stores && !stores.isEmpty()){
//            for(Store store : stores){
//                JSONObject storeJo = JsonUtil.toJSONObject(store, new String[]{"id", "name"});
//                storeJa.add(storeJo);
//            }
//        }
//        initJo.put("stores", storeJa);

            //商品规格列表
//        List<Specification> specifications = specificationService.findAll();
//        initJo.put("specifications", specificationService.createEntity().convertList(specifications, null));

        } catch (Exception e) {
            LOG.error("getAddProductInitInfo error. " + e);
            return new PublicResult<>(PublicResultConstant.FAILED, initJo);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, initJo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存商品", notes = "body体参数,不需要Authorization", produces = "application/json")
    @ApiImplicitParams({})
    @Log(description = "企业中心保存商品接口:/business/product/save")
    @ParamXssPass
    public PublicResult<Boolean> save(@CurrentUser Member member, @RequestBody JSONObject productJson) {
        LOG.info("save product param : productJson = " + productJson.toString());
        try {
            Product product = JsonUtil.toBean(productJson.toJSONString(), Product.class);
//            Store store = storeService.find(member.getStore().getId());
//            product.setStore(store);

            for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
                ProductImage productImage = iterator.next();
                productImage.setLarge(productImage.getSource());
                productImage.setMedium(productImage.getSource());
                productImage.setThumbnail(productImage.getSource());
                if (productImage == null || productImage.getSource() == null || productImage.getSource().isEmpty()) {
                    iterator.remove();
                    continue;
                }
            }
            product.setProductCategory(productCategoryService.find(product.getProductCategory().getId()));
            product.setBrand(brandService.find(product.getBrand().getId()));

            //标签
            List<Long> tagIdList = new ArrayList<>();
            JSONArray tagJa = productJson.getJSONArray("tagIds");
            for (int i = 0; i < tagJa.size(); i++) {
                tagIdList.add(tagJa.getLong(i));
            }
            product.setTags(new HashSet<Tag>(tagService.findListByIDList(tagIdList)));

            if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
                return new PublicResult<Boolean>("商品编码重复。", false);
            }
            if (product.getMarketPrice() == null) {
                BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
                product.setMarketPrice(defaultMarketPrice);
            }
            if (product.getPoint() == null) {
                long point = calculateDefaultPoint(product.getPrice());
                product.setPoint(point);
            }

            product.setFullName(null);
            product.setAllocatedStock(0);
            product.setScore(0F);
            product.setTotalScore(0L);
            product.setScoreCount(0L);
            product.setHits(0L);
            product.setWeekHits(0L);
            product.setMonthHits(0L);
            product.setSales(0L);
            product.setWeekSales(0L);
            product.setMonthSales(0L);
            product.setWeekHitsDate(new Date());
            product.setMonthHitsDate(new Date());
            product.setWeekSalesDate(new Date());
            product.setMonthSalesDate(new Date());
            product.setReviews(null);
            product.setFavoriteMembers(null);
            product.setCartItems(null);
            product.setOrderItems(null);
            product.setGiftItems(null);
            product.setProductNotifies(null);

            //商品运费策略
            if (product.getIsStoreFreight()) {
                product.setProductFreights(null);
            }

            //商品配送区域设置
            switch (product.getProductDelivery()) {
                case all:
                    product.setProductAreas(null);
                    product.setProductStations(null);
                    break;
                case area:
                    product.setProductStations(null);
                    break;
                case station:
                    product.setProductAreas(null);
                    break;

                default:
                    product.setProductAreas(null);
                    product.setProductStations(null);
                    break;
            }

            Collections.sort(product.getProductImages());
            if (StringUtils.isBlank(product.getImage()) && StringUtils.isNotBlank(product.getThumbnail())) {
                product.setImage(product.getThumbnail());
            }

            //处理商品参数
            JSONArray parameterGroupList = productJson.getJSONArray("parameterGroupList");
            for (int i = 0; i < parameterGroupList.size(); i++) {
                JSONObject pgJo = parameterGroupList.getJSONObject(i);
                JSONArray parameters = pgJo.getJSONArray("parameters");
                for (int j = 0; j < parameters.size(); j++) {
                    JSONObject parameterJo = parameters.getJSONObject(j);
                    String parameterValue = parameterJo.getString("value");
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(parameterValue)) {
                        Parameter parameter = JsonUtil.toBean(parameterJo.toJSONString(), Parameter.class);
                        product.getParameterValue().put(parameter, parameterValue);
                    }
                }
            }

            //处理商品属性
            JSONArray attributeList = productJson.getJSONArray("attributeList");
            for (int i = 0; i < attributeList.size(); i++) {
                JSONObject aJo = attributeList.getJSONObject(i);
                String value = aJo.getString("value");
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
//                    Attribute attribute = JsonUtil.toBean(aJo.toJSONString(), Attribute.class);
                    Attribute attribute = attributeService.find(aJo.getLong("id"));
                    product.setAttributeValue(attribute, value);
                }
            }

            Goods goods = new Goods();
            List<Product> products = new ArrayList<Product>();
            JSONArray specifications = productJson.getJSONArray("specificationList");

//            String cityCode = store.getCityCode();//cityService.getCityCodeByMember(member);
//            LOG.info("Method save cityCode:"+cityCode);
            if (specifications != null && !specifications.isEmpty()) {
                for (int i = 0; i < specifications.size(); i++) {
                    JSONArray specificationTemps = specifications.getJSONArray(i);
                    for (int j = 0; j < specificationTemps.size(); j++) {
                        if (j == 0) {
                            if (i == 0) {
                                product.setGoods(goods);
                                product.setSpecifications(new HashSet<Specification>());
                                product.setSpecificationValues(new HashSet<SpecificationValue>());
//                                product.setCityCode(cityCode);
                                products.add(product);
                            } else {
                                Product specificationProduct = new Product();
                                BeanUtils.copyProperties(product, specificationProduct);
                                specificationProduct.setId(null);
                                specificationProduct.setCreateDate(null);
                                specificationProduct.setModifyDate(null);
                                specificationProduct.setSn(null);
                                specificationProduct.setFullName(null);
                                specificationProduct.setAllocatedStock(0);
                                specificationProduct.setIsList(false);
                                specificationProduct.setScore(0F);
                                specificationProduct.setTotalScore(0L);
                                specificationProduct.setScoreCount(0L);
                                specificationProduct.setHits(0L);
                                specificationProduct.setWeekHits(0L);
                                specificationProduct.setMonthHits(0L);
                                specificationProduct.setSales(0L);
                                specificationProduct.setWeekSales(0L);
                                specificationProduct.setMonthSales(0L);
                                specificationProduct.setWeekHitsDate(new Date());
                                specificationProduct.setMonthHitsDate(new Date());
                                specificationProduct.setWeekSalesDate(new Date());
                                specificationProduct.setMonthSalesDate(new Date());
                                specificationProduct.setGoods(goods);
                                specificationProduct.setReviews(null);
//                                specificationProduct.setConsultations(null);
                                specificationProduct.setFavoriteMembers(null);
                                specificationProduct.setSpecifications(new HashSet<Specification>());
                                specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
//                                specificationProduct.setPromotions(null);
                                specificationProduct.setCartItems(null);
                                specificationProduct.setOrderItems(null);
                                specificationProduct.setGiftItems(null);
                                specificationProduct.setProductNotifies(null);

                                products.add(specificationProduct);
                            }
                        }
                        Product specificationProduct = products.get(i);
                        JSONObject specificationTemp = specificationTemps.getJSONObject(j);
//                        specificationProduct.setCityCode(cityCode);
                        specificationProduct.getSpecifications().add(specificationService.find(specificationTemp.getLong("specificationId")));
                        specificationProduct.getSpecificationValues().add(specificationValueService.find(specificationTemp.getLong("specificationValueId")));
                    }
                }
            } else {
                product.setGoods(goods);
                product.setSpecifications(null);
                product.setSpecificationValues(null);
//                product.setCityCode(cityCode);
                products.add(product);
            }
            goods.getProducts().clear();
            goods.getProducts().addAll(products);
            try {
                goodsService.save(goods);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("save e. " + e);
                return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save product error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新商品", notes = "body体参数,不需要Authorization", produces = "application/json")
    @ApiImplicitParams({})
    @Log(description = "企业中心更新商品接口:/business/product/update")
    @ParamXssPass
    public PublicResult<Boolean> update(@CurrentUser Member member, @RequestBody JSONObject productJson) {
        LOG.info("update product param : productJson = " + productJson.toString());
        Product product = JsonUtil.toBean(productJson.toJSONString(), Product.class);
//        Store store = storeService.find(member.getStore().getId());
//        product.setStore(store);
        try {

            for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
                ProductImage productImage = iterator.next();
                productImage.setLarge(productImage.getSource());
                productImage.setMedium(productImage.getSource());
                productImage.setThumbnail(productImage.getSource());
                if (productImage == null || productImage.getSource() == null || productImage.getSource().isEmpty()) {
                    iterator.remove();
                    continue;
                }
            }
            product.setProductCategory(productCategoryService.find(product.getProductCategory().getId()));
            product.setBrand(brandService.find(product.getBrand().getId()));

            //标签
            List<Long> tagIdList = new ArrayList<>();
            JSONArray tagJa = productJson.getJSONArray("tagIds");
            for (int i = 0; i < tagJa.size(); i++) {
                tagIdList.add(tagJa.getLong(i));
            }
            product.setTags(new HashSet<Tag>(tagService.findListByIDList(tagIdList)));

            Product pProduct = productService.find(product.getId());
            if (pProduct == null) {
                return new PublicResult<Boolean>("商品不存在。", false);
            }
            if (StringUtils.isNotEmpty(product.getSn()) && !productService.snUnique(pProduct.getSn(), product.getSn())) {
                return new PublicResult<Boolean>("商品编码重复。", false);
            }
            if (product.getMarketPrice() == null) {
                BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
                product.setMarketPrice(defaultMarketPrice);
            }
            if (product.getPoint() == null) {
                long point = calculateDefaultPoint(product.getPrice());
                product.setPoint(point);
            }

            //商品运费策略
            if (product.getIsStoreFreight()) {
                product.setProductFreights(null);
            }
            //商品配送区域设置
            switch (product.getProductDelivery()) {
                case all:
                    product.setProductAreas(null);
                    product.setProductStations(null);
                    break;
                case area:
                    product.setProductStations(null);
                    break;
                case station:
                    product.setProductAreas(null);
                    break;
                default:
                    product.setProductAreas(null);
                    product.setProductStations(null);
                    break;
            }

            Collections.sort(product.getProductImages());
            if (StringUtils.isBlank(product.getImage()) && StringUtils.isNotBlank(product.getThumbnail())) {
                product.setImage(product.getThumbnail());
            }

            //处理商品参数
            JSONArray parameterGroupList = productJson.getJSONArray("parameterGroupList");
            for (int i = 0; i < parameterGroupList.size(); i++) {
                JSONObject pgJo = parameterGroupList.getJSONObject(i);
                JSONArray parameters = pgJo.getJSONArray("parameters");
                for (int j = 0; j < parameters.size(); j++) {
                    JSONObject parameterJo = parameters.getJSONObject(j);
                    String parameterValue = parameterJo.getString("value");
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(parameterValue)) {
                        Parameter parameter = JsonUtil.toBean(parameterJo.toJSONString(), Parameter.class);
                        product.getParameterValue().put(parameter, parameterValue);
                    }
                }
            }

            //处理商品属性
            JSONArray attributeList = productJson.getJSONArray("attributeList");
            for (int i = 0; i < attributeList.size(); i++) {
                JSONObject aJo = attributeList.getJSONObject(i);
                String value = aJo.getString("value");
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(value)) {
//                    Attribute attribute = JsonUtil.toBean(aJo.toJSONString(), Attribute.class);
                    Attribute attribute = attributeService.find(aJo.getLong("id"));
                    product.setAttributeValue(attribute, value);
                }
            }
            Goods goods = pProduct.getGoods();
            List<Product> products = new ArrayList<Product>();
            productService.convertSpecProducts(products, product, pProduct, productJson, null);
            if(null == products){
                return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
            }

            goods.getProducts().clear();
            goods.getProducts().addAll(products);
            goodsService.update(goods);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("update product error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }


    /**
     * 删除
     */
    @GetMapping("/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete product param : ids = " + ids);
        try {
            productService.delete(ids);
        } catch (Exception e) {
            LOG.error("delete product error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 复制
     */
    @GetMapping("/copy")
    public PublicResult<Boolean> copy(Long id, @CurrentUser Member member) {
        try {
            Product srcProduct = productService.find(id);
            if (srcProduct == null) {
                return new PublicResult<Boolean>("商品不存在，复制失败！", false);
            }
//            Setting setting = SettingUtils.get();
            Product product = new Product();
            BeanUtils.copyProperties(srcProduct, product);
            product.setPrice(product.getPrice());
            product.setMarketPrice(product.getMarketPrice());
            for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
                ProductImage productImage = iterator.next();
                productImage.setLarge(productImage.getSource());
                productImage.setMedium(productImage.getSource());
                productImage.setThumbnail(productImage.getSource());
                if (productImage == null || productImage.getSource() == null || productImage.getSource().isEmpty()) {
                    iterator.remove();
                    continue;
                }
            }
            product.setIsMarketable(false);
            product.setIsLimit(false);
            product.setId(null);
            product.setCreateDate(null);
            product.setModifyDate(null);
            product.setSn(null);
            product.setFullName(null);
            product.setScore(0F);
            product.setTotalScore(0L);
            product.setScoreCount(0L);
            product.setHits(0L);
            product.setWeekHits(0L);
            product.setMonthHits(0L);
            product.setSales(0L);
            product.setWeekSales(0L);
            product.setMonthSales(0L);
            product.setWeekHitsDate(new Date());
            product.setMonthHitsDate(new Date());
            product.setWeekSalesDate(new Date());
            product.setMonthSalesDate(new Date());
            product.setReviews(null);
//            product.setConsultations(null);
            product.setFavoriteMembers(null);
//            product.setPromotions(null);
            product.setCartItems(null);
            product.setOrderItems(null);
            product.setProductNotifies(null);
            product.setPayments(null);
//            product.setBespeaks(null);
//            product.setSupervisorOrderItems(null);

            Set<Tag> tags = new HashSet<Tag>();
            for (Tag tag : srcProduct.getTags()) {
                tags.add(tag);
            }
            product.setTags(tags);

            List<ProductImage> productImages = new ArrayList<ProductImage>();
            for (ProductImage productImage : srcProduct.getProductImages()) {
                productImages.add(productImage);
            }
            product.setProductImages(productImages);

            List<ProductFreight> productFreights = new ArrayList<ProductFreight>();
            for (ProductFreight productFreight : srcProduct.getProductFreights()) {
                productFreights.add(productFreight);
            }
            product.setProductFreights(productFreights);

            List<ProductArea> productAreas = new ArrayList<ProductArea>();
            for (ProductArea productArea : srcProduct.getProductAreas()) {
                productAreas.add(productArea);
            }
            product.setProductAreas(productAreas);

            List<ProductStation> productStations = new ArrayList<ProductStation>();
            for (ProductStation productStation : srcProduct.getProductStations()) {
                productStations.add(productStation);
            }
            product.setProductStations(productStations);

            Map<Parameter, String> parameterValue = new HashMap<Parameter, String>();
            for (Parameter parameter : srcProduct.getParameterValue().keySet()) {
                parameterValue.put(parameter, srcProduct.getParameterValue().get(parameter));
            }
            product.setParameterValue(parameterValue);

            Set<GiftItem> giftItems = new HashSet<GiftItem>();
            for (GiftItem giftItem : srcProduct.getGiftItems()) {
                giftItems.add(giftItem);
            }
            product.setGiftItems(giftItems);

//            Map<MemberRank, BigDecimal> memberRankPriceMap = new HashMap<MemberRank, BigDecimal>();
//            for (MemberRank memberRank : srcProduct.getMemberPrice().keySet()) {
//                memberRankPriceMap.put(memberRank, srcProduct.getMemberPrice().get(memberRank));
//            }
//            product.setMemberPrice(memberRankPriceMap);

            Map<Integer, BigDecimal> wholesalePriceMap = new HashMap<Integer, BigDecimal>();
            for (Integer weight : srcProduct.getWholesalePrice().keySet()) {
                wholesalePriceMap.put(weight, srcProduct.getWholesalePrice().get(weight));
            }
            product.setWholesalePrice(wholesalePriceMap);

            HashSet<Specification> specifications = new HashSet<Specification>();
            for (Specification specification : srcProduct.getSpecifications()) {
                specifications.add(specification);
            }
            product.setSpecifications(specifications);

            HashSet<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();
            for (SpecificationValue specificationValue : srcProduct.getSpecificationValues()) {
                specificationValues.add(specificationValue);
            }
            product.setSpecificationValues(specificationValues);

//            product.setHistorys(null);

            //商品运费策略
            if (product.getIsStoreFreight()) {
                product.setProductFreights(null);
            }

            //商品配送区域设置
            switch (product.getProductDelivery()) {
                case all:
                    product.setProductAreas(null);
                    product.setProductStations(null);
                    break;
                case area:
                    product.setProductStations(null);
                    break;
                case station:
                    product.setProductAreas(null);
                    break;

                default:
                    product.setProductAreas(null);
                    product.setProductStations(null);
                    break;
            }

            Collections.sort(product.getProductImages());
            if (product.getImage() == null && product.getThumbnail() != null) {
                product.setImage(product.getThumbnail());
            }

//            Set<RelationProduct> relationProducts = new HashSet<>();
//            for (RelationProduct relationProduct : product.getRelationProducts()) {
//                RelationProduct rp = new RelationProduct();
//                rp.setProductId(relationProduct.getProductId());
//                rp.setProductName(relationProduct.getProductName());
//                rp.setPrice(relationProduct.getPrice());
//                rp.setOrder(relationProduct.getOrder());
//                rp.setRelation(product);
//                relationProducts.add(rp);
//            }
//            product.setRelationProducts(relationProducts);

            Goods goods = new Goods();
            List<Product> products = new ArrayList<Product>();
            product.setGoods(goods);

//            product.setCityCode(cityService.getCityCodeByMember(member));
//            LOG.info("Method copy cityCode:"+product.getCityCode());
            products.add(product);
            goods.getProducts().clear();
            goods.getProducts().addAll(products);
            goodsService.save(goods);
        } catch (Exception e) {
            LOG.error("copy error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 商品异步上下架
     *
     * @param sn
     * @return
     */
    @GetMapping("/updateMarketable")
    public PublicResult<Boolean> updateMarketable(String sn, @CurrentUser Member member) {
        try {
            if (StringUtils.isEmpty(sn)) {
                return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
            }
            Product product = productService.findBySn(sn);
            if (null == product) {
                return new PublicResult<Boolean>("商品不存在，操作失败！", false);
            }
            if (product.getIsMarketable()) {
                product.setIsMarketable(false);
            } else {
                product.setIsMarketable(true);
            }
//            product.setCityCode(cityService.getCityCodeByMember(member));
//            LOG.info("Method updateMarketable cityCode:"+product.getCityCode());
            productService.update(product);
            return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
        } catch (Exception e) {
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
    }

    /**
     * 计算默认市场价
     *
     * @param price 价格
     */
    private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
        return price.multiply(new BigDecimal(1.2)).setScale(2);
//        Setting setting = SettingUtils.get();
//        Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
//        return setting.setScale(price.multiply(new BigDecimal(defaultMarketPriceScale.toString())));
    }

    /**
     * 计算默认积分
     *
     * @param price 价格
     */
    private long calculateDefaultPoint(BigDecimal price) {
//        Setting setting = SettingUtils.get();
//        Double defaultPointScale = setting.getDefaultPointScale();
        return price.multiply(new BigDecimal(1)).longValue();
    }

    /**
     * 设置关联商品
     *
     * @param product
     */
    private void setRelationProduct(Product product) {
//        Set<RelationProduct> relationProducts = product.getRelationProducts();
//        for (RelationProduct temp : product.getRelationProducts()) {
//            temp.setRelation(product);
//        }
    }


    @ApiOperation(value = "关键字分页查询商品列表", notes = "关键字分页查询商品列表", produces = "application/json")
    @Log(description = "关键字分页查询商品列表接口:/business/product/select_products")
    @GetMapping("/select_products")
    public PageResult selectProducts(Pageable pageable, String keyword,
                                     Boolean isMarketable,
                                     Boolean isGift,
                                     ProductType productType,
                                     @RequestParam(value = "excludeIds[]",required = false) Long[] excludeIds,
                                     @CurrentUser Member member) {
        JSONArray data;
        try {
            Page page = productService.search(pageable, keyword, isGift, isMarketable, productType,excludeIds);
            data = productService.createEntity().convertList(page.getContent(), new String[]{"id","sn","fullName","image","path","price","store.id"});
            return new PageResult<>((int) page.getTotal(), page.getPageNumber(), page.getPageSize(), data);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PromotionController Method productSelect exception!! params: "
                    + "keyword:" + keyword);
            return new PageResult<>(0, pageable.getPageNumber(), pageable.getPageSize(), new ArrayList<>());
        }

    }


}
