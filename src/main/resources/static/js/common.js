document.addEventListener('DOMContentLoaded', function () {
  // --- 페이지 로드 시 기존 인증 정보 강제 정리 ---
  if (window.location.pathname === '/members/login' || window.location.pathname
      === '/members/register') {
    document.cookie = "Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    localStorage.removeItem('accessToken');
  }

  // --- 회원가입 및 로그인 처리 ---
  const registerForm = document.getElementById('register-form');
  if (registerForm) {
    registerForm.addEventListener('submit', handleAuthFormSubmit);
  }

  const loginForm = document.getElementById('login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', handleAuthFormSubmit);
  }

  // --- 위시리스트 페이지일 경우, 데이터 로드 함수 실행 ---
  const wishlistBody = document.getElementById('wishlist-body');
  if (wishlistBody) {
    fetchWishlist();
  }

  // --- 이벤트 위임을 사용한 통합 이벤트 리스너 ---
  document.body.addEventListener('click', function (event) {
    // [추가] 상세 페이지 이동 로직
    const clickableRow = event.target.closest('.clickable-row');
    if (clickableRow) {
      const link = clickableRow.dataset.link;
      if (link) {
        window.location.href = link;
      }
      return; // 다른 클릭 이벤트와 중복되지 않도록 여기서 종료
    }

    // 위시리스트 추가 버튼
    if (event.target.classList.contains('add-to-wishlist-btn')) {
      handleWishlistAction(event);
    }
    // 위시리스트 삭제 버튼
    if (event.target.classList.contains('delete-from-wishlist-btn')) {
      handleWishlistAction(event);
    }
  });

  // --- [삭제] 기존 상세 페이지 이동 로직은 위의 통합 리스너로 이전했습니다. ---
  // document.querySelectorAll(".clickable-row").forEach(...)

  // --- 삭제 확인 ---
  document.querySelectorAll(".delete-form").forEach(form => {
    form.addEventListener("click", function (event) {
      event.stopPropagation();
    });

    form.addEventListener("submit", function (event) {
      if (!confirm("정말 삭제하시겠습니까?")) {
        event.preventDefault();
      }
    });
  });
});

// 로그인/회원가입 폼 제출 비동기 함수
async function handleAuthFormSubmit(event) {
  event.preventDefault();
  const form = event.target;
  const url = form.action;
  const email = form.email.value;
  const password = form.password.value;

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({email, password})
    });

    const result = await response.json();

    if (response.ok) {
      localStorage.setItem('accessToken', result.token);
      const successMsg = (url.includes('register')) ? '회원가입 성공!' : '로그인 성공!';
      alert(successMsg);
      window.location.href = '/members/products';
    } else {
      const errorMsg = (result.message) ? result.message : '오류가 발생했습니다.';
      alert(`${(url.includes('register')) ? '회원가입' : '로그인'} 실패: ${errorMsg}`);
    }
  } catch (error) {
    console.error('Error:', error);
    alert('요청 중 오류가 발생했습니다.');
  }
}

// 위시리스트 추가/삭제 비동기 함수
async function handleWishlistAction(event) {
  event.stopPropagation();
  const button = event.target;
  const productId = button.dataset.productId;
  const wishId = button.dataset.wishId;
  const token = localStorage.getItem('accessToken');
  const url = (productId) ? '/api/wishes' : `/api/wishes/${wishId}`;
  const method = (productId) ? 'POST' : 'DELETE';

  if (!token) {
    alert('로그인이 필요합니다.');
    window.location.href = '/members/login';
    return;
  }

  if (method === 'DELETE' && !confirm('정말 삭제하시겠습니까?')) {
    return;
  }

  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: (productId) ? JSON.stringify({productId: productId}) : null
    });

    if (response.ok) {
      const successMsg = (method === 'POST') ? '위시리스트에 상품을 추가했습니다!'
          : '위시리스트에서 상품을 삭제했습니다.';
      alert(successMsg);
      if (method === 'DELETE') {
        window.location.reload();
      }
    } else {
      const error = await response.json();
      alert(`요청 실패: ${error.message}`);
    }
  } catch (error) {
    console.error('Error:', error);
    alert('요청 중 오류가 발생했습니다.');
  }
}

// 위시리스트 데이터를 가져와 렌더링하는 함수
async function fetchWishlist() {
  const token = localStorage.getItem('accessToken');
  const wishlistBody = document.getElementById('wishlist-body');

  if (!token) {
    alert('로그인이 필요합니다.');
    window.location.href = '/members/login';
    return;
  }

  try {
    const response = await fetch(`/api/wishes`, {
      headers: {'Authorization': token}
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message);
    }

    const items = await response.json();
    wishlistBody.innerHTML = '';

    if (items.length === 0) {
      wishlistBody.innerHTML = '<tr><td colspan="4">위시리스트에 담긴 상품이 없습니다.</td></tr>';
      return;
    }

    items.forEach(item => {
      // [수정] 상품명, 가격, 이미지 td에 clickable-row 클래스와 data-link 속성을 추가합니다.
      const row = `
        <tr>
          <td class="clickable-row" data-link="/members/products/${item.product.id}">${item.product.name}</td>
          <td class="clickable-row" data-link="/members/products/${item.product.id}">${item.product.price}원</td>
          <td class="clickable-row" data-link="/members/products/${item.product.id}"><img src="${item.product.imageUrl}" alt="이미지 없음"/></td>
          <td>
            <button class="delete-from-wishlist-btn" data-wish-id="${item.id}">삭제</button>
          </td>
        </tr>
      `;
      wishlistBody.innerHTML += row;
    });

  } catch (error) {
    console.error('Error:', error);
    wishlistBody.innerHTML = `<tr><td colspan="4">위시리스트를 불러오는 중 오류 발생: ${error.message}</td></tr>`;
  }
}