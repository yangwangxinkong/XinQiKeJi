<template>
  <article-category-detail :is-edit='false'></article-category-detail>
</template>

<script>
  import ArticleCategoryDetail from './components/ArticleCategoryDetail'

  export default {
    name: 'createForm',
    components: { ArticleCategoryDetail }
  }
</script>

