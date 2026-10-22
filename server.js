const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const benchmarkHtmlPath = path.join(__dirname, 'app', 'src', 'main', 'assets', 'benchmark_3d.html');
const apkPath = path.join(__dirname, '.build-outputs', 'app-debug.apk');

const server = http.createServer((req, res) => {
  const url = (req.url || '/').split('?')[0];

  if (url === '/health' || url === '/api/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', engine: 'THREEJS_CANNON_PBR_V1' }));
    return;
  }

  if (url === '/download/app-debug.apk' && fs.existsSync(apkPath)) {
    const stat = fs.statSync(apkPath);
    res.writeHead(200, {
      'Content-Type': 'application/vnd.android.package-archive',
      'Content-Length': stat.size,
      'Content-Disposition': 'attachment; filename="app-debug.apk"'
    });
    fs.createReadStream(apkPath).pipe(res);
    return;
  }

  // Serve the 3D Vehicle Physics & PBR Graphics Benchmark
  fs.readFile(benchmarkHtmlPath, 'utf8', (err, html) => {
    if (err) {
      res.writeHead(500, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('Error loading benchmark_3d.html');
      return;
    }
    res.writeHead(200, {
      'Content-Type': 'text/html; charset=utf-8',
      'Cache-Control': 'no-cache'
    });
    res.end(html);
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`3D Vehicle Physics & PBR Graphics Benchmark server listening on http://0.0.0.0:${PORT}`);
});
