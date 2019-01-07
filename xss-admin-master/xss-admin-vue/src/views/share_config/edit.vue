<template>
  <share-config-detail :is-edit='true'></share-config-detail>
</template>

<script>
  import shareConfigDetail from './shareConfig/shareConfigDetail'

  export default {
    name: 'editForm',
    components: { shareConfigDetail }
  }
</script>
