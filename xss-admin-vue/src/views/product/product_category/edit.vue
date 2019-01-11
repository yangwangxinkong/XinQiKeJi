<template>
  <product-category-detail :is-edit='true'></product-category-detail>
</template>

<script>
import ProductCategoryDetail from './components/ProductCategoryDetail'

export default {
  name: 'createForm',
  components: { ProductCategoryDetail }
}
</script>

