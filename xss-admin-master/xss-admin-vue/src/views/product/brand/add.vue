<template>
  <brand-detail :is-edit='false'></brand-detail>
</template>

<script>
import BrandDetail from './components/BrandDetail'

export default {
  name: 'createForm',
  components: { BrandDetail }
}
</script>

