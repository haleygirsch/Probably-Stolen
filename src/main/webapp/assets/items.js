/* Locally indexed items with screenshots cropped into PNG sprites. No network calls. */
(() => {
 'use strict';
 const root=document.getElementById('catalog-app');
 if(!root)return;
 const rows=Array.isArray(window.PS_ITEMS)?window.PS_ITEMS:[];
 const $=id=>document.getElementById(id);
 const search=$('catalog-search'),type=$('catalog-type'),legality=$('catalog-legal'),price=$('catalog-price'),sort=$('catalog-sort');
 const count=$('catalog-count'),result=$('catalog-results'),modal=$('item-detail'),detail=$('item-detail-body');
 const state={page:1,view:'cards',ascending:true};
 const perPage=20;
 const esc=value=>String(value??'').replace(/[&<>"']/g,ch=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[ch]));
 const format=value=>value===null||value===undefined?'Price not listed':Number(value).toLocaleString()+' Credit'+(value===1?'':'s');
 const art=row=>row.image?`<img src="/assets/images/items/${encodeURIComponent(row.image)}" alt="${esc(row.name)} pixel-art item icon" loading="lazy" decoding="async">`:'<div class="item-art-missing"><span aria-hidden="true">◇</span><small>Artwork coming later</small></div>';
 const legtext=l=>l==='No'?'Not contraband':l+'-Level Contraband';
 const legclass=l=>l==='No'?'legal':l.toLowerCase();
 function priceMatches(p,v){if(v==='all')return true;if(v==='unknown')return p===null;if(p===null)return false;return v==='low'?p<25:v==='medium'?p>=25&&p<100:v==='high'?p>=100&&p<500:p>=500;}
 function filter(){
  const query=search.value.toLowerCase().trim();
  let selected=rows.filter(row=>(!query||[row.name,row.type,row.legality].join(' ').toLowerCase().includes(query))&&(!type.value||row.type===type.value)&&(!legality.value||row.legality===legality.value)&&priceMatches(row.price,price.value)&&(!$('catalog-illustrated').checked||!!row.image));
  const cmp=(a,b)=>{
   if(sort.value==='price'){
    if(a.price===null)return b.price===null?nameCmp(a,b):1;
    if(b.price===null)return -1;
    return (a.price-b.price)*(state.ascending?1:-1)||nameCmp(a,b);
   }
   if(sort.value==='category')return a.type.localeCompare(b.type)*(state.ascending?1:-1)||nameCmp(a,b);
   return nameCmp(a,b)*(state.ascending?1:-1);
  };
  selected.sort(cmp);return selected;
 }
 function nameCmp(a,b){return a.name.localeCompare(b.name,undefined,{numeric:true,sensitivity:'base'});}
 function card(row){return `<article class="item-card"><div class="item-art">${art(row)}</div><div class="item-card-body"><h3 class="item-card-title">${esc(row.name)}</h3><div class="item-card-meta"><span class="item-price">${esc(format(row.price))}</span><span class="item-type">${esc(row.type)}</span></div><span class="item-legal ${legclass(row.legality)}">${esc(legtext(row.legality))}</span><button type="button" class="item-card-button" data-item="${esc(row.name)}">View item details ↗</button></div></article>`;}
 function table(items){return `<div class="item-table-scroll"><table class="item-table"><thead><tr><th>Icon / Name</th><th>Price</th><th>Type</th><th>Legality</th><th></th></tr></thead><tbody>${items.map(row=>`<tr><td><div class="item-table-name">${row.image?art(row):'<span class="item-mini-noart">◇</span>'}<strong>${esc(row.name)}</strong></div></td><td>${esc(format(row.price))}</td><td>${esc(row.type)}</td><td><span class="item-legal ${legclass(row.legality)}">${esc(legtext(row.legality))}</span></td><td><button type="button" data-item="${esc(row.name)}">Details</button></td></tr>`).join('')}</tbody></table></div>`;}
 function render(){
  const matching=filter(),pages=Math.max(1,Math.ceil(matching.length/perPage));
  state.page=Math.min(state.page,pages);
  const shown=matching.slice((state.page-1)*perPage,state.page*perPage);
  count.textContent=`${matching.length} of ${rows.length} items`;
  $('catalog-page').textContent=`Page ${state.page} of ${pages}`;
  $('catalog-prev').disabled=state.page===1;
  $('catalog-next').disabled=state.page===pages;
  $('catalog-direction').textContent=state.ascending?'A → Z':'Z → A';
  document.querySelectorAll('[data-catalog-view]').forEach(el=>{const yes=el.dataset.catalogView===state.view;el.classList.toggle('is-active',yes);el.setAttribute('aria-pressed',String(yes));});
  result.innerHTML=shown.length?(state.view==='cards'?`<div class="item-card-grid">${shown.map(card).join('')}</div>`:table(shown)):'<div class="item-no-results"><strong>No items found ♡</strong><p>Try another search or filter.</p></div>';
 }
 function showDetail(name){const row=rows.find(x=>x.name===name);if(!row)return;
  detail.innerHTML=`<div class="item-detail-layout"><div class="item-detail-art">${art(row)}</div><div><h2>${esc(row.name)}</h2><dl><div><dt>Base price</dt><dd>${esc(format(row.price))}</dd></div><div><dt>Type / Category</dt><dd>${esc(row.type)}</dd></div><div><dt>Legality</dt><dd>${esc(legtext(row.legality))}</dd></div><div><dt>Artwork source</dt><dd>${row.image?'Your uploaded item screenshot':'No screenshot received yet'}</dd></div></dl><p style="font-size:12px;color:#9d7090">Additional item details, recipes and location information can be added later.</p></div></div>`;
  modal.showModal();
 }
 const categories=[...new Set(rows.map(row=>row.type))].sort();
 for(const item of categories){const option=document.createElement('option');option.value=item;option.textContent=`${item} (${rows.filter(row=>row.type===item).length})`;type.append(option);}
 $('item-total-pill').textContent=`${rows.length} locally indexed items`;
 $('item-art-pill').textContent=`${rows.filter(row=>row.image).length} screenshot icon matches`;
 for(const elem of [search,type,legality,price,sort,$('catalog-illustrated')])elem.addEventListener(elem===search?'input':'change',()=>{state.page=1;render();});
 $('catalog-clear').addEventListener('click',()=>{search.value='';state.page=1;render();search.focus();});
 $('catalog-direction').addEventListener('click',()=>{state.ascending=!state.ascending;state.page=1;render();});
 document.querySelectorAll('[data-catalog-view]').forEach(btn=>btn.addEventListener('click',()=>{state.view=btn.dataset.catalogView;state.page=1;render();}));
 $('catalog-prev').addEventListener('click',()=>{state.page=Math.max(1,state.page-1);render();result.scrollIntoView({block:'start',behavior:'smooth'});});
 $('catalog-next').addEventListener('click',()=>{state.page++;render();result.scrollIntoView({block:'start',behavior:'smooth'});});
 result.addEventListener('click',e=>{const btn=e.target.closest('[data-item]');if(btn)showDetail(btn.dataset.item);});
 $('item-detail-close').addEventListener('click',()=>modal.close());
 modal.addEventListener('click',e=>{if(e.target===modal)modal.close();});
 render();
})();
