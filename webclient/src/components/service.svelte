<script>
    export let data;
    export let full;
</script>


<div class="uk-card uk-card-hover uk-card-default">

    {#if data.service_images != null}
        {#each data.service_images as image}
            <img src="http://localhost:5021/image/{image.image_id}">
        {/each}
    {/if}
    <div class="uk-card-body">
        <h3 class="uk-card-title"><a href="#">{data.service_title}</a></h3>
        <p>{data.service_description}</p>
        <button class="uk-button uk-button-primary">Lisätiedot</button>
        <button class="uk-button uk-button-secondary">Tilaa</button>
        {#if full}
            <hr>
            Palvelun tarjoaa: <strong>{data.service_provider_name}</strong>
            |
            Hinta: <strong>{data.service_price}€</strong>
        {/if}
    </div>
</div>
