/* Probably Stolen: customer directory and sprite browser.
   All features work offline. See customers-data.js for editable local records. */
(() => {
  'use strict';
  const root = document.getElementById('customers-dashboard');
  if (!root) return;

  const customers = Array.isArray(window.PS_CUSTOMER_DATA) ? window.PS_CUSTOMER_DATA : [];
  const portraits = Array.isArray(window.PS_PORTRAITS) ? window.PS_PORTRAITS : [];
  const $ = id => document.getElementById(id);
  const search = $('customer-search');
  const results = $('customer-results');
  const modal = $('customer-detail');
  const modalBody = $('customer-detail-body');
  const perPage = 16;
  const factions = ['Black Market','Lower Levels','Revolution','Sector 13 Cartel','Security','Tourist','Upper Levels','No faction','Unverified'];
  const state = {section:'directory', kind:'All', faction:'All', group:'All', source:'All', search:'', action:'all', sort:'name',portraitSort:'group', asc:true, view:'cards', page:1};

  const esc = text => String(text ?? '').replace(/[&<>"']/g, x => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[x]));
  const numericCompare = (a,b) => String(a).localeCompare(String(b),undefined,{numeric:true,sensitivity:'base'});
  const slugCounts = (arr, prop, value) => arr.filter(row => value==='All' || (prop==='faction' ? (row.factions || [row.faction]).includes(value) : row[prop]===value)).length;
  const avatar = c => c.portrait ? `/assets/images/customers/${encodeURIComponent(c.portrait)}.png` : '';
  const image = (url,label) => url ? `<img src="${esc(url)}" alt="${esc(label)}" loading="lazy" decoding="async">` : '<span class="customer-placeholder" aria-hidden="true">♡</span>';
  const budgetValue = c => c.budget ? Number(String(c.budget).replace(/,/g,'').match(/\d+/)?.[0] || 0) : null;
  const sells = c => Number.isInteger(c.sellsCount) && c.sellsCount > 0;
  const hasInfo = c => !!(c.budget||c.buys||c.note||c.behavior||sells(c));
  const portraitLabel = c => c.portraitExact ? 'Named character portrait' : c.portrait ? 'Example appearance' : 'Portrait not yet matched';
  const shortFacts = c => {
    const data=[];
    if(c.buys)data.push(`Buys ${c.buys}`);
    if(sells(c))data.push(`Sells ${c.sellsCount} item${c.sellsCount===1?'':'s'}`);
    if(c.behavior)data.push(c.behavior);
    if(!data.length && c.note)data.push(c.note);
    if(!data.length)data.push('More information to be verified');
    return data;
  };
  const groups = ['All','Named & special','Lower Levels','Upper Levels','Security','Black Market','Revolution','Wanted','Augs','Engineers','Medical','Mercenaries','Farmers','Chefs','Scientists'];
  const groupMatch = (row,v) => v==='All' || row.group===v || row.group.startsWith(v+' ·');
  const sourceMatch = (row,v) => v==='All' || (v==='Uploaded' ? row.quality==='transparent' : row.quality!=='transparent');

  function pill(title,count,active,filter,value){
    return `<button type="button" class="customer-filter-pill ${active?'is-active':''}" data-filter-group="${esc(filter)}" data-filter-value="${esc(value)}" aria-pressed="${active}">${esc(title)} <span class="customer-pill-count">${count}</span></button>`;
  }
  function buildPills(){
    $('customer-types').innerHTML=['All','Regular','Story'].map(v=>pill(v,slugCounts(customers,'kind',v),state.kind===v,'kind',v)).join('');
    $('customer-factions').innerHTML=['All',...factions].filter(v=>v==='All' || slugCounts(customers,'faction',v)>0).map(v=>pill(v==='All'?'All factions':v,slugCounts(customers,'faction',v),state.faction===v,'faction',v)).join('');
    const listed=groups.filter(v=>v==='All' || portraits.some(row=>groupMatch(row,v)));
    $('portrait-groups').innerHTML=listed.map(v=>pill(v==='All'?'All portraits':v,portraits.filter(row=>groupMatch(row,v)).length,state.group===v,'group',v)).join('');
  }
  function matchesCustomer(c,q){
    return [c.name,c.faction,...(c.factions||[]),c.kind,c.buys,c.budget,c.note,c.behavior,c.portrait,c.sellsCount].join(' ').toLowerCase().includes(q);
  }
  function filtered(){
    const q=state.search.trim().toLocaleLowerCase();
    let rows = state.section==='directory' ? customers.filter(c =>
      (state.kind==='All'||c.kind===state.kind) &&
      (state.faction==='All'||(c.factions||[c.faction]).includes(state.faction)) &&
      (!q||matchesCustomer(c,q)) &&
      (state.action==='all'||(state.action==='buyers'&&!!c.buys)||(state.action==='sellers'&&sells(c))||
       (state.action==='known'&&hasInfo(c))||(state.action==='illustrated'&&!!c.portrait))
    ) : portraits.filter(p=>
      groupMatch(p,state.group)&&sourceMatch(p,state.source)&&
      (!q||[p.id,p.spriteId,p.group,p.sourceName].join(' ').toLowerCase().includes(q))
    );
    const compare = (a,b) => {
      if(state.section==='portraits'){
        if(state.portraitSort==='id')return numericCompare(a.id,b.id);
        if(state.portraitSort==='source')return (a.quality==='transparent'?0:1)-(b.quality==='transparent'?0:1)||numericCompare(a.id,b.id);
        return numericCompare(a.group,b.group)||numericCompare(a.id,b.id);
      }
      if(state.sort==='budget'){
        const aa=budgetValue(a),bb=budgetValue(b);
        if(aa===null&&bb!==null)return 1;
        if(bb===null&&aa!==null)return -1;
        return ((aa??0)-(bb??0))*(state.asc?1:-1)||numericCompare(a.name,b.name);
      }
      if(state.sort==='faction')return numericCompare(a.faction,b.faction)*(state.asc?1:-1)||numericCompare(a.name,b.name);
      return numericCompare(a.name,b.name)*(state.asc?1:-1);
    };
    rows.sort(compare);
    if(state.section==='portraits' && !state.asc)rows.reverse();
    return rows;
  }
  function card(c){
    const facts=shortFacts(c);
    return `<article class="customer-card"><div class="customer-portrait-frame">${image(avatar(c),c.portraitExact?c.name+' game character portrait':'Example appearance for '+c.name)}
       <span class="customer-avatar-credit">${esc(portraitLabel(c))}</span></div>
       <div class="customer-card-main"><div class="customer-card-top"><span class="customer-kind ${c.kind==='Story'?'story':''}">${esc(c.kind)}</span></div>
       <h3>${esc(c.name)}</h3><div class="customer-card-faction"><span aria-hidden="true" class="faction-dot" data-faction="${esc(c.faction)}"></span>${esc(c.faction==='Unverified'?'Faction unverified':c.faction)}</div>
       <div class="customer-budget">${c.budget?esc(c.budget)+' credits':'Budget not listed'}</div>
       <p class="customer-card-desc">${facts.map(esc).join(' · ')}</p>
       <button type="button" class="customer-detail-button" data-detail="${esc(c.name)}">View details <span aria-hidden="true">↗</span></button></div></article>`;
  }
  function table(rows){
    return `<div class="customer-table-wrap"><table class="customer-table"><thead><tr><th>Portrait</th><th>Customer</th><th>Faction</th><th>Type</th><th>Budget</th><th>Buys / sells</th><th></th></tr></thead><tbody>`+
    rows.map(c=>`<tr><td class="customer-table-portrait">${image(avatar(c),c.name)}</td><td><strong>${esc(c.name)}</strong></td>
       <td>${esc(c.faction)}</td><td>${esc(c.kind)}</td><td>${c.budget?esc(c.budget)+' credits':'—'}</td>
       <td>${shortFacts(c).map(esc).join(' · ')}</td><td><button class="customer-small-detail" type="button" data-detail="${esc(c.name)}">Details</button></td></tr>`).join('')+'</tbody></table></div>';
  }
  function portraitCard(p){
    return `<button type="button" class="portrait-gallery-card" data-portrait="${esc(p.id)}">
      <span class="portrait-gallery-image">${image(p.url,p.id)}</span><strong>${esc(p.id)}</strong>
      <small>${esc(p.group)}</small>${p.quality==='transparent'?'<span class="portrait-quality">Transparent PNG</span>':''}</button>`;
  }
  function portraitTable(rows){
    return `<div class="customer-table-wrap"><table class="customer-table"><thead><tr><th>Portrait</th><th>Sprite ID</th><th>Group</th><th>Image source</th><th></th></tr></thead><tbody>`+
      rows.map(p=>`<tr><td class="customer-table-portrait">${image(p.url,p.id)}</td><td><strong>${esc(p.id)}</strong></td><td>${esc(p.group)}</td><td>User-supplied PNG</td><td><button type="button" class="customer-small-detail" data-portrait="${esc(p.id)}">Enlarge</button></td></tr>`).join('')+'</tbody></table></div>';
  }
  function detailCard(name){
    const c=customers.find(x=>x.name===name);if(!c)return;
    const faction=(c.factions?.length>1)?c.factions.join(' / '):c.faction;
    const portraitText=c.portraitExact?'Named portrait':c.portrait?'Representative pool appearance':'No portrait assigned';
    modalBody.innerHTML=`<div class="customer-detail-layout"><div class="customer-detail-portrait">${image(avatar(c),c.name)}</div><div class="customer-detail-info">
      <span class="customer-kind ${c.kind==='Story'?'story':''}">${esc(c.kind)} customer</span><h2>${esc(c.name)}</h2>
      <dl><div><dt>Faction</dt><dd>${esc(faction==='Unverified'?'Not verified for this entry':faction)}</dd></div>
      <div><dt>Budget</dt><dd>${c.budget?esc(c.budget)+' credits':'Not listed in the directory'}</dd></div>
      <div><dt>Buys</dt><dd>${esc(c.buys||'Not listed')}</dd></div>
      <div><dt>Sells</dt><dd>${sells(c)?'Up to '+c.sellsCount+' item types listed':'Not listed'}</dd></div>
      <div><dt>Behavior</dt><dd>${esc(c.behavior||'Not specified')}</dd></div>
      <div><dt>Notes</dt><dd>${esc(c.note||'No additional notes yet')}</dd></div>
      <div><dt>Portrait</dt><dd>${esc(portraitText)}</dd></div></dl>
      ${c.portrait&&!c.portraitExact?'<p class="customer-art-note">This is an example sprite from a character pool. The same customer type can appear with multiple portraits.</p>':''}
      <div class="customer-source-links"><a class="customer-detail-link" href="https://wikily.gg/probably-stolen/customers" target="_blank" rel="noopener noreferrer">Wikily customer directory ↗</a>
      <a class="customer-detail-link" href="https://probablystolen.wiki.gg/wiki/Customers" target="_blank" rel="noopener noreferrer">wiki.gg customer roles ↗</a></div></div></div>`;
    modal.showModal();
  }
  function detailPortrait(id){
    const p=portraits.find(x=>x.id===id);if(!p)return;
    const source='Original transparent PNG from customers.zip';
    modalBody.innerHTML=`<div class="customer-gallery-detail"><div class="customer-gallery-large">${image(p.url,p.id)}</div>
     <h2>${esc(p.id)}</h2><p>${esc(p.group)} · ${esc(source)}</p>
     ${p.sourceName?`<p class="portrait-source-filename">Original filename: ${esc(p.sourceName)}</p>`:''}
     <small>These are portrait sprite IDs or artwork labels. Generic appearances may be reused by several customer types.</small></div>`;
    modal.showModal();
  }
  function render(){
    buildPills();
    const gallery=state.section==='portraits';
    $('customer-directory-filters').hidden=gallery;
    $('customer-portrait-filters').hidden=!gallery;
    $('customer-action').parentElement.hidden=gallery;
    $('customer-sort').parentElement.hidden=gallery;
    $('customer-portrait-sort').parentElement.hidden=!gallery;
    const rows=filtered(),total=rows.length,maxPage=Math.max(1,Math.ceil(total/perPage));
    state.page=Math.min(state.page,maxPage);
    const shown=rows.slice((state.page-1)*perPage,state.page*perPage);
    $('customer-result-count').textContent=total+(gallery?' portraits':' customers');
    $('customer-found').textContent=gallery?`${total} of ${portraits.length} portrait variants`:`${total} of ${customers.length} customers`;
    $('customer-sort-direction').textContent=state.asc?'A → Z':'Z → A';
    $('customer-gallery-summary').textContent=`${portraits.length} customer ZIP PNGs, including alternative appearances · no screenshot crops`;
    document.querySelectorAll('[data-customer-section]').forEach(el=>{const active=el.dataset.customerSection===state.section;el.classList.toggle('is-active',active);el.setAttribute('aria-pressed',String(active));});
    document.querySelectorAll('[data-customer-view]').forEach(el=>{const active=el.dataset.customerView===state.view;el.classList.toggle('is-active',active);el.setAttribute('aria-pressed',String(active));});
    if(!rows.length)results.innerHTML='<div class="customer-no-results"><strong>No matches found ♡</strong><p>Try another search or a different filter.</p><button id="customer-reset" type="button">Clear filters</button></div>';
    else if(gallery)results.innerHTML=state.view==='cards'?`<div class="portrait-gallery-grid">${shown.map(portraitCard).join('')}</div>`:portraitTable(shown);
    else results.innerHTML=state.view==='cards'?`<div class="customer-card-grid">${shown.map(card).join('')}</div>`:table(shown);
    $('customer-prev').disabled=state.page<=1;
    $('customer-next').disabled=state.page>=maxPage;
    $('customer-page-label').textContent=`Page ${state.page} of ${maxPage}`;
  }
  function reset(){state.kind='All';state.faction='All';state.group='All';state.source='All';state.search='';state.action='all';state.page=1;search.value='';$('customer-action').value='all';render();}
  search.addEventListener('input',()=>{state.search=search.value;state.page=1;render();});
  $('customer-clear').addEventListener('click',()=>{search.value='';state.search='';state.page=1;search.focus();render();});
  $('customer-sort').addEventListener('change',e=>{state.sort=e.target.value;state.page=1;render();});
  $('customer-portrait-sort').addEventListener('change',e=>{state.portraitSort=e.target.value;state.page=1;render();});
  $('customer-action').addEventListener('change',e=>{state.action=e.target.value;state.page=1;render();});
  $('customer-sort-direction').addEventListener('click',()=>{state.asc=!state.asc;state.page=1;render();});
  document.querySelectorAll('[data-customer-section]').forEach(button=>button.addEventListener('click',()=>{state.section=button.dataset.customerSection;state.page=1;render();}));
  document.querySelectorAll('[data-customer-view]').forEach(button=>button.addEventListener('click',()=>{state.view=button.dataset.customerView;state.page=1;render();}));
  root.addEventListener('click',e=>{
    const btn=e.target.closest('button');if(!btn)return;
    if(btn.id==='customer-reset'){reset();return;}
    if(btn.dataset.filterGroup){state[btn.dataset.filterGroup]=btn.dataset.filterValue;state.page=1;render();return;}
    if(btn.dataset.detail){detailCard(btn.dataset.detail);return;}
    if(btn.dataset.portrait){detailPortrait(btn.dataset.portrait);return;}
  });
  $('customer-prev').addEventListener('click',()=>{state.page=Math.max(1,state.page-1);render();results.scrollIntoView({block:'start',behavior:'smooth'});});
  $('customer-next').addEventListener('click',()=>{state.page++;render();results.scrollIntoView({block:'start',behavior:'smooth'});});
  $('customer-detail-close').addEventListener('click',()=>modal.close());
  modal.addEventListener('click',e=>{if(e.target===modal)modal.close();});
  render();
})();
