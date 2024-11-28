function listagemProdutos() {
    $.get("http://localhost:3000/listagem-produtos", function (data, status) {

        for (i = 0; i < data.length; i++) {
            $('#info').append("<ul class='list-group list-group-flush'>" +
                "<li class='list-group-item bg-dark text-white'>" +
                "    <strong>ID: </strong>" + data[i].id + 
                "</li>" + 
                "<li class='list-group-item bg-dark text-white'>" +
                "    <strong>Nome: </strong>" + data[i].nome +
                "</li>" +
                "<li class='list-group-item bg-dark text-white'>" +
                "    <strong>Categoria: </strong>" + data[i].categoria +
                "</li>" +
                "<li class='list-group-item bg-dark text-white'>" +
                "    <strong>Quantidade: </strong>" + data[i].quantidade +
                "</li>" +
                "<li class='list-group-item bg-dark text-white'>" +
                "    <strong>Preço: </strong>" + data[i].preco +
                "</li>" +
                "<li class='list-group-item bg-dark text-white'>" +
                "    <strong>Descrição: </strong>" + data[i].descricao +
                "</li>" +
            "</ul>" +
            "<div class='botoes'>" +
                "<a id='btnMovimentacao' href= >Movimentação</a>" +
                "<a id='btnAtualizar' href='atualizar-produto.html?id=" + data[i].id + "'>Atualizar</a>" +
                "<a id='btnDeletar' href='' onclick='deletarProduto(" + data[i].id + ")' class='btn btn-danger'>Deletar</a>" +
            "</div>" +
            "<br><br>"
            )
        }
    });
}

function filtrarProdutos(event) {
    event.preventDefault();

    const nomeFiltro = $('#nomePesquisa').val().toLowerCase();

    $.get("http://localhost:3000/listagem-produtos", function (data, status) {
        $('#info').empty();

        for (let i = 0; i < data.length; i++) {
            if (data[i].nome.toLowerCase().includes(nomeFiltro)) {
                $('#info').append(
                    "<ul class='list-group list-group-flush'>" +
                        "<li class='list-group-item bg-dark text-white'>" +
                            "<strong>ID: </strong>" + data[i].id +
                        "</li>" +
                        "<li class='list-group-item bg-dark text-white'>" +
                            "<strong>Nome: </strong>" + data[i].nome +
                        "</li>" +
                        "<li class='list-group-item bg-dark text-white'>" +
                            "<strong>Categoria: </strong>" + data[i].categoria +
                        "</li>" +
                        "<li class='list-group-item bg-dark text-white'>" +
                            "<strong>Quantidade: </strong>" + data[i].quantidade +
                        "</li>" +
                        "<li class='list-group-item bg-dark text-white'>" +
                            "<strong>Preço: </strong>" + data[i].preco +
                        "</li>" +
                        "<li class='list-group-item bg-dark text-white'>" +
                            "<strong>Descrição: </strong>" + data[i].descricao +
                        "</li>" +
                    "</ul>" +
                    "<div class='botoes'>" +
                        "<a id='btnMovimentacao' href='#'>Movimentação</a>" +
                        "<a id='btnAtualizar' href='atualizar-produto.html?id=" + data[i].id + "'>Atualizar</a>" +
                        "<a id='btnDeletar' href='#' onclick='deletarProduto(" + data[i].id + ")' class='btn btn-danger'>Deletar</a>" +
                    "</div>" +
                    "<br><br>"
                );
            }
        }
    });
}

function deletarProduto(id) {
    if (confirm("Deseja realmente deletar este produto?")) {
        $.ajax({
            type: 'DELETE',
            url: `http://localhost:3000/listagem-produtos/${id}`,
            success: function () {
                alert('Produto deletado com sucesso!');
                location.reload();
            },
            error: function (xhr, status, error) {
                console.error("Erro ao tentar deletar produto:", status, error); 
                alert('Erro ao tentar deletar o produto.' + id);
            }
        });
    }
}