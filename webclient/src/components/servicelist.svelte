<script>
    import { onMount } from 'svelte';
    import Api from '../services/api.js';

    import Service from './service.svelte';

    let services = []

    onMount(() => {
        Api.services((response) => {
            services = response.data.data;
        });
    });

</script>

<h2>Palvelut</h2>
{#each services as service}
    <Service data={service} full={false}/>
{/each}
