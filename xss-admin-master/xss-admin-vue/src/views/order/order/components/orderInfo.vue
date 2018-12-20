<template>
  <table border fit highlight-current-row style="width: 100%">

    <tr align="center" v-loading="loading"
                     element-loading-text="请给我点时间！">
      <td><span>{{$t('order.sn')}}</span></td>
      <td><span>{{order.sn}}</span></td>
      <td><span>{{$t('order.createDate')}}</span></td>
      <td><span>{{order.createDate}}</span></td>
    </tr>
    <tr align="center">
      <td><span>{{$t('order.sn')}}</span></td>
      <td><span>{{order.sn}}</span></td>
      <td><span>{{$t('order.createDate')}}</span></td>
      <td><span>{{order.createDate}}</span></td>
    </tr>

  </table>
</template>

<script>
import { fetchList } from '@/api/order'

export default {
  props: {
    type: {
      type: String,
      default: 'Order'
    }
  },
  data() {
    return {
      order: null,
      listQuery: {
        type: this.type
      },
      loading: false
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.$emit('create') // for test
      const id = this.$route.params && this.$route.params.id

      fetchList(id).then(response => {
        this.order = response.data.data
        this.loading = false
      })
    }
  }
}
</script>

