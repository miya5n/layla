var signin = new Vue({
  el: '#signin_table',
  methods: {
    toAccountEntry: function (e) {
      $(this.$el).slideUp()
      $(account.$el).delay(500).slideDown()
    }
  }
});
var account = new Vue({
  el: '#account_table',
  methods: {
    toSignin: function (e) {
      $(this.$el).slideUp()
      $(signin.$el).delay(500).slideDown()
    }
  }
});
