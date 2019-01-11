<template>
  <wx-menu-detail :is-edit='false'></wx-menu-detail>
</template>

<script>
  import WxMenuDetail from './components/WxMenuDetail'

  export default {
    name: 'createForm',
    components: { WxMenuDetail }
  }
</script>

