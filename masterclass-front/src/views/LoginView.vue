<script setup lang="ts">

</script>

<template>
<body>
  <h1>Connexion</h1>
  <div class="login-form">
    <form>
      <div class="email-form">
        <label for="email">Email</label>
        <input type="text" placeholder="Ex:name.surname@imt-atlantique.net">
      </div>
      <div class="password-form">
        <label for="password">Password</label>
        <input type="password" placeholder="••••••••••••••••••••"></input>
      </div>
      <button class="login-button" type="submit">Login</button>
    </form>
  </div>
</body>
</template>

<style scoped>
body {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  justify-content: center;
  min-height: 100vh;
}

h1 {
  font-size: 3rem;
}

form {
  width:300px;
}

.email-form, .password-form {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  text-align: left;
}
.login-button {
  padding: 10px 20px;
  background-color: #00786F;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
}

input{
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

input::placeholder {
  color: gray;
}

</style>
