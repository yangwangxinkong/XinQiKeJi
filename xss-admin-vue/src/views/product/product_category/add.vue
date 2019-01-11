<template>
  <product-category-detail :is-edit='false'></product-category-detail>
</template>

<script>
import ProductCategoryDetail from './components/ProductCategoryDetail'

export default {
  name: 'createForm',
  components: { ProductCategoryDetail }
}
</script>

