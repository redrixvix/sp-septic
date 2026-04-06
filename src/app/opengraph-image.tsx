import { ImageResponse } from "next/og";

export const size = { width: 1200, height: 630 };
export const contentType = "image/png";

export default function OpenGraphImage() {
  return new ImageResponse(
    (
      <div
        style={{
          width: "100%",
          height: "100%",
          display: "flex",
          flexDirection: "column",
          alignItems: "flex-start",
          justifyContent: "flex-end",
          backgroundColor: "#0f172a",
          padding: "60px 80px",
          fontFamily: "system-ui, sans-serif",
        }}
      >
        {/* Background grid pattern */}
        <div
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundImage:
              "linear-gradient(rgba(30,58,95,0.3) 1px, transparent 1px), linear-gradient(90deg, rgba(30,58,95,0.3) 1px, transparent 1px)",
            backgroundSize: "60px 60px",
          }}
        />

        {/* Accent shape */}
        <div
          style={{
            position: "absolute",
            top: -80,
            right: -80,
            width: 400,
            height: 400,
            borderRadius: "50%",
            background:
              "radial-gradient(circle, rgba(220,38,38,0.25) 0%, transparent 70%)",
          }}
        />

        {/* Top accent line */}
        <div
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            height: "6px",
            background: "linear-gradient(90deg, #DC2626 0%, #1E3A5F 50%, #DC2626 100%)",
          }}
        />

        {/* Company name — large */}
        <div
          style={{
            position: "relative",
            display: "flex",
            flexDirection: "column",
            alignItems: "flex-start",
          }}
        >
          {/* Logo mark */}
          <div
            style={{
              display: "flex",
              alignItems: "center",
              gap: "16px",
              marginBottom: "24px",
            }}
          >
            <div
              style={{
                width: "64px",
                height: "64px",
                borderRadius: "12px",
                background: "linear-gradient(135deg, #DC2626 0%, #B91C1C 100%)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <span
                style={{
                  color: "white",
                  fontSize: "36px",
                  fontWeight: "900",
                  letterSpacing: "-2px",
                }}
              >
                S&P
              </span>
            </div>
            <div
              style={{
                display: "flex",
                flexDirection: "column",
              }}
            >
              <span
                style={{
                  color: "white",
                  fontSize: "42px",
                  fontWeight: "900",
                  letterSpacing: "-1px",
                  lineHeight: 1,
                }}
              >
                S&P Septic &
              </span>
              <span
                style={{
                  color: "#DC2626",
                  fontSize: "42px",
                  fontWeight: "900",
                  letterSpacing: "-1px",
                  lineHeight: 1,
                }}
              >
                Excavating
              </span>
            </div>
          </div>

          {/* Tagline */}
          <div
            style={{
              color: "#94A3B8",
              fontSize: "24px",
              fontWeight: "400",
              marginBottom: "12px",
            }}
          >
            Septic & Excavating | Warren, Ohio
          </div>

          {/* Trust badges */}
          <div style={{ display: "flex", gap: "16px", marginTop: "8px" }}>
            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: "8px",
                background: "rgba(255,255,255,0.08)",
                border: "1px solid rgba(255,255,255,0.15)",
                borderRadius: "8px",
                padding: "8px 14px",
              }}
            >
              <div
                style={{
                  color: "#D4A443",
                  fontSize: "16px",
                  fontWeight: "900",
                }}
              >
                A+
              </div>
              <span style={{ color: "white", fontSize: "14px", fontWeight: "500" }}>
                BBB Accredited
              </span>
            </div>
            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: "8px",
                background: "rgba(255,255,255,0.08)",
                border: "1px solid rgba(255,255,255,0.15)",
                borderRadius: "8px",
                padding: "8px 14px",
              }}
            >
              <span style={{ color: "#60A5FA", fontSize: "14px" }}>✓</span>
              <span style={{ color: "white", fontSize: "14px", fontWeight: "500" }}>
                Licensed & Insured
              </span>
            </div>
            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: "8px",
                background: "rgba(255,255,255,0.08)",
                border: "1px solid rgba(255,255,255,0.15)",
                borderRadius: "8px",
                padding: "8px 14px",
              }}
            >
              <span style={{ color: "#60A5FA", fontSize: "14px" }}>✓</span>
              <span style={{ color: "white", fontSize: "14px", fontWeight: "500" }}>
                Family Owned
              </span>
            </div>
          </div>

          {/* Phone number */}
          <div
            style={{
              marginTop: "24px",
              color: "white",
              fontSize: "28px",
              fontWeight: "700",
              letterSpacing: "0.5px",
            }}
          >
            (330) 979-3930
          </div>
        </div>
      </div>
    ),
    {
      ...size,
    }
  );
}
