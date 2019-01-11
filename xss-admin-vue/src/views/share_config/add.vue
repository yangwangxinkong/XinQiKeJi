<template>
  <share-config-detail :is-edit='false'></share-config-detail>
</template>

<script>
  import shareConfigDetail from './shareConfig/shareConfigDetail'

  export default {
    name: 'createForm',
    components: { shareConfigDetail }
  }
</script>
