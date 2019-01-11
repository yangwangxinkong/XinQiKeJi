<template>
  <brand-detail :is-edit='true'></brand-detail>
</template>

<script>
import BrandDetail from './components/BrandDetail'

export default {
  name: 'createForm',
  components: { BrandDetail }
}
</script>

