<template>
    <div>
        <h1>Article</h1>
        <button @click="session">button</button>
    </div>
</template>

<script>
    import ls from '@/utils/sessionStorageUtil.js'
export default {
    methods: {
        session() {
            ls.setItem("activeProgressEnum", -1)
        }
    }
};
</script>

<style>
</style>