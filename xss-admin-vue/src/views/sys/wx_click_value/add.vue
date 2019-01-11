<template>
  <wx-click-value-detail :is-edit='false'></wx-click-value-detail>
</template>

<script>
  import WxClickValueDetail from './components/WxClickValueDetail'

  export default {
    name: 'createForm',
    components: { WxClickValueDetail }
  }
</script>

