<template>
  <article-category-detail :is-edit='true'></article-category-detail>
</template>

<script>
import ArticleCategoryDetail from './components/ArticleCategoryDetail'

export default {
  name: 'createForm',
  components: { ArticleCategoryDetail }
}
</script>

