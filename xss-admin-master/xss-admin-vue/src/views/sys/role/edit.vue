<template>
  <role-detail :is-edit='true'></role-detail>
</template>

<script>
import roleDetail from './components/roleDetail'

export default {
  name: 'editForm',
  components: { roleDetail }
}
</script>

