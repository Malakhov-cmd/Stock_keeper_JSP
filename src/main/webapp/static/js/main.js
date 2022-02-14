/* globals Chart:false, feather:false */


function uploadFile(file) {
    let url = 'https://api.cloudinary.com/v1_1/test/image/upload'
    let xhr = new XMLHttpRequest()
    let formData = new FormData()

    xhr.open('POST', url, true)
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest')

    formData.append('upload_preset', 'test')
    xhr.onload = function() {
        console.log(xhr.response)
    };
    xhr.send()
}

let labelsValues = [
    'Sunday',
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday'
];

let dataSetValues = [
    15339,
    21345,
    18483,
    24003,
    23489,
    24092,
    12034
];

(function (labelsValues, dataSetValues) {
    'use strict'

    feather.replace({'aria-hidden': 'true'})

    // Graphs
    var ctx = document.getElementById('myChart')
    // eslint-disable-next-line no-unused-vars
    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labelsValues,
            datasets: [{
                data: dataSetValues,
                lineTension: 0,
                backgroundColor: 'transparent',
                borderColor: '#007bff',
                borderWidth: 4,
                pointBackgroundColor: '#007bff'
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false
            }
        }
    })
})()
