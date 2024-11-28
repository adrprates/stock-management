function configurarFormulario() {
    const produtoId = new URLSearchParams(window.location.search).get('id');
    
    if (produtoId) {
        $('#formulario').attr('action', `http://localhost:3000/listagem-produtos/${produtoId}`);
        carregarDadosProduto(produtoId);
    } else {
        alert("ID do produto não encontrado!");
    }
}

function carregarDadosProduto(id) {
    $.get(`http://localhost:3000/listagem-produtos/${id}`, function(data) {
        $('#nome').val(data.nome);
        $('#categoria').val(data.categoria);
        $('#quantidade').val(data.quantidade);
        $('#preco').val(data.preco);
        $('#descricao').val(data.descricao);
    }).fail(function() {
        alert("Erro ao carregar os dados do produto.");
    });
}

function atualizarProduto(event) {
    event.preventDefault();

    let produto = {
        nome: $('#nome').val(),
        categoria: $('#categoria').val(),
        quantidade: $('#quantidade').val(),
        preco: $('#preco').val(),
        descricao: $('#descricao').val()
    };

    $.ajax({
        type: 'PUT',
        url: $('#formulario').attr('action'),
        data: JSON.stringify(produto),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(response) {
            alert('Produto atualizado com sucesso!');
            setTimeout(() => {
                window.location.href = '/templates/listagem-produtos.html';
            }, 0);
        },
        error: function(xhr, status, error) {
            alert('Erro ao tentar atualizar o produto.');
            console.error("Erro ao tentar atualizar produto:", status, error);
        }
    });
}

$(document).ready(function() {
    configurarFormulario();
    
    $("#formulario").submit(atualizarProduto);
});