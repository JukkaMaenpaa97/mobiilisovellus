<script>
    import { onMount } from 'svelte';
    import Api from '../services/api.js';

    import Service from './service.svelte';

    let services = []
    let loading = true;

    onMount(() => {
        Api.services((response) => {
            services = response.data.data;
            loading = false;
        });
    });

</script>

<h2>Palvelut {#if loading}<div uk-spinner></div>{/if}</h2>

{#each services as service}
    <Service data={service} full={false}/>
{/each}
