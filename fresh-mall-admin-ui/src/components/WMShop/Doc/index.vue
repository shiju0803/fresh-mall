<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'WMShopDoc',
  data() {
    return {
      url: 'https://gitee.com/JavaLionLi/fresh-mall/wikis/pages'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
