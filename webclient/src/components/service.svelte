<script>
    export let data;
    export let full;
</script>


<h3><a href="#">{data.service_title}</a></h3>
<p>{data.service_description}</p>
<button class="uk-button uk-button-primary">Lisätiedot</button>
<button class="uk-button uk-button-secondary">Tilaa</button>
{#if full}
    <hr>
    Palvelun tarjoaa: <strong>{data.service_provider_name}</strong>
    |
    Hinta: <strong>{data.service_price}€</strong>
{/if}
