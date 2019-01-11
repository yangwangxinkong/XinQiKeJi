<template>
  <wx-click-key-detail :is-edit='false'></wx-click-key-detail>
</template>

<script>
  import WxClickKeyDetail from './components/WxClickKeyDetail'

  export default {
    name: 'createForm',
    components: { WxClickKeyDetail }
  }
</script>

